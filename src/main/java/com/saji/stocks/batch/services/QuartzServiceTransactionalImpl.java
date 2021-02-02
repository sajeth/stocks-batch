/**
 *
 */
package com.saji.stocks.batch.services;

import com.saji.stocks.batch.dto.DefaultParameterDTO;
import com.saji.stocks.batch.services.model.Instance;
import com.saji.stocks.batch.services.model.Status;
import com.saji.stocks.batch.services.model.Statuses;
import com.saji.stocks.core.repository.batch.BatchJobRepository;
import com.saji.stocks.entities.batch.DefaultParameter;
import com.saji.stocks.entities.batch.DefaultParameterId;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author sajeth
 *
 */
@SuppressWarnings("exports")
@Service
@Transactional
public class QuartzServiceTransactionalImpl implements QuartzServiceTransactional {

    private static final String JOB_STATUS_STARTED = "STARTED";
    private static final String JOB_STATUS_COMPLETED = "COMPLETED";
    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().toString());
    @Autowired
    private BatchJobRepository batchJobRepository;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void forceCompleteMostRecentRun(final String jobName) {
        Optional<Integer> lastRun = batchJobRepository.getMostRecentJobExectionIdByStatusAndJobname(JOB_STATUS_STARTED,
                jobName);
        if (lastRun.isPresent()) {
            batchJobRepository.updateJobStepExecution(JOB_STATUS_COMPLETED, JOB_STATUS_COMPLETED,
                    BigInteger.valueOf(lastRun.get()));
        }
    }

    @Override
    public Statuses kickOffJob(String jobName, Map<String, String> environmentVariables) throws SchedulerException {
        final Map<String, JobKey> jobMap = fetchFullJobMap(scheduler);
        final JobKey jobKey = getJobKeyByName(jobMap, jobName);
        this.scheduler.triggerJob(jobKey);
        List<JobExecutionContext> runningJobs = scheduler.getCurrentlyExecutingJobs();
        final Status status = fetchJobStatus(scheduler, jobKey, runningJobs);
        return new Statuses(Arrays.asList(status));
    }

    /**
     * @param scheduler
     * @param jobKey
     * @param runningJobs
     * @return
     * @throws SchedulerException
     */
    private Status fetchJobStatus(final Scheduler scheduler, final JobKey jobKey,
                                  final List<JobExecutionContext> runningJobs) throws SchedulerException {
        final Status status = new Status();
        final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        scheduler.getTriggersOfJob(jobKey).stream().forEach(trigger -> {
            TriggerState triggerState = null;
            try {
                triggerState = scheduler.getTriggerState(trigger.getKey());
            } catch (SchedulerException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
            Instance instance = new Instance();
            instance.setPreviousFireTime(trigger.getPreviousFireTime());
            instance.setFinalFireTime(trigger.getFinalFireTime());
            instance.setNextFireTime(trigger.getNextFireTime());
            instance.setFireable(trigger.mayFireAgain());
            instance.setPriority(trigger.getPriority());
            instance.setStatus(triggerState.toString());
            instance.setRunning(isJobRunning(jobKey, runningJobs));
            instance.setMisfireCode(trigger.getMisfireInstruction());
            status.setName(jobKey.getName());
            status.setDescription(jobDetail.getDescription());
            status.addInstance(instance);
        });
        return status;
    }

    private boolean isJobRunning(final JobKey jobKey, final List<JobExecutionContext> runningJobs) {
        return runningJobs.stream().anyMatch(runningJob -> jobKey.equals(runningJob.getJobDetail().getKey()));
    }

    /**
     * @param jobMap
     * @param jobName
     * @return
     */
    private JobKey getJobKeyByName(final Map<String, JobKey> jobMap, final String jobName) {
        if (jobMap.containsKey(jobName)) {
            return jobMap.get(jobName);
        }
        final Optional<String> jobMapKey = jobMap.keySet().stream().filter(key -> key.contains(jobName)).findFirst();
        return jobMapKey.map(jobMap::get).orElse(null);
    }

    /**
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    private Map<String, JobKey> fetchFullJobMap(Scheduler scheduler) throws SchedulerException {

        final Map<String, JobKey> jobMap = new HashMap<>();
        scheduler.getJobGroupNames().stream().forEach(gn -> {
            try {
                scheduler.getJobKeys(GroupMatcher.jobGroupEquals(gn)).stream()
                        .forEach(jobKey -> jobMap.put(jobKey.getName(), jobKey));
            } catch (SchedulerException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        });
        return jobMap;
    }

    @Override
    public Statuses retrieveStatuses() throws SchedulerException {

        final Map<String, JobKey> jobMap = fetchFullJobMap(this.scheduler);
        List<JobExecutionContext> runningJobs = this.scheduler.getCurrentlyExecutingJobs();
        return new Statuses(jobMap.values().stream().map(jobKey -> {
            try {
                return fetchJobStatus(this.scheduler, jobKey, runningJobs);
            } catch (SchedulerException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
            return null;
        }).collect(Collectors.toList()));
    }

    @Override
    public Statuses retrieveStatus(final String jobName) throws SchedulerException {

        final Map<String, JobKey> jobMap = fetchFullJobMap(this.scheduler);
        List<JobExecutionContext> runningJobs = this.scheduler.getCurrentlyExecutingJobs();
        final JobKey jobKey = getJobKeyByName(jobMap, jobName);
        final Status status = fetchJobStatus(scheduler, jobKey, runningJobs);
        return new Statuses(Arrays.asList(status));
    }

    @Override
    public Statuses rescheduleJob(final String jobName, final Map<String, String> bodyRequest)
            throws SchedulerException, ParseException {
        final Map<String, JobKey> jobMap = fetchFullJobMap(this.scheduler);
        List<JobExecutionContext> runningJobs = this.scheduler.getCurrentlyExecutingJobs();
        final JobKey jobKey = getJobKeyByName(jobMap, jobName);
        final String newCronExpression = bodyRequest.get("cronExpression");
        final String triggerName = bodyRequest.get("triggerName");
        final TriggerKey triggerKey = new TriggerKey(triggerName);
        final CronTriggerImpl trigger = (CronTriggerImpl) scheduler.getTrigger(triggerKey);
        trigger.setCronExpression(newCronExpression);
        scheduler.rescheduleJob(triggerKey, trigger);
        final Status status = fetchJobStatus(scheduler, jobKey, runningJobs);
        return new Statuses(Arrays.asList(status));
    }

    @Override
    public Statuses pauseJob(final String jobName) throws SchedulerException {
        final Map<String, JobKey> jobMap = fetchFullJobMap(this.scheduler);
        final JobKey jobKey = getJobKeyByName(jobMap, jobName);
        scheduler.pauseJob(jobKey);
        return new Statuses(Arrays.asList(fetchJobStatus(scheduler, jobKey, scheduler.getCurrentlyExecutingJobs())));
    }

    @Override
    public Statuses resumeJob(final String jobName) throws SchedulerException {
        final Map<String, JobKey> jobMap = fetchFullJobMap(this.scheduler);
        final JobKey jobKey = getJobKeyByName(jobMap, jobName);
        scheduler.resumeJob(jobKey);
        return new Statuses(Arrays.asList(fetchJobStatus(scheduler, jobKey, scheduler.getCurrentlyExecutingJobs())));
    }

    @Override
    public Statuses killJob(final String jobName) throws SchedulerException {
        final Map<String, JobKey> jobMap = fetchFullJobMap(this.scheduler);
        final JobKey jobKey = getJobKeyByName(jobMap, jobName);
        scheduler.interrupt(jobKey);
        return new Statuses(
                Arrays.asList(fetchJobStatus(this.scheduler, jobKey, this.scheduler.getCurrentlyExecutingJobs())));
    }

    @Override
    public List<DefaultParameterDTO> fetchParametersForJob(final String jobName) {
        return convertDefaultParametersToDTOList(batchJobRepository.getBatchDefaultParameters(jobName));
    }

    private List<DefaultParameterDTO> convertDefaultParametersToDTOList(List<DefaultParameter> parameters) {
        return parameters.stream().map(this::convertDefaultParameterToDTO).collect(Collectors.toList());
    }

    private DefaultParameterDTO convertDefaultParameterToDTO(final DefaultParameter parameter) {
        return new DefaultParameterDTO(parameter.getJobName(), parameter.getKey(), parameter.getValue());

    }

    @Override
    public List<DefaultParameterDTO> saveParametersForJob(String jobName, String key, String value) {
        DefaultParameterId parameterId = new DefaultParameterId(jobName, key);
        DefaultParameter parameter = batchJobRepository.getBatchDefaultParameter(parameterId);
        boolean parameterExists = parameter != null;
        if (parameterExists) {
            parameter.setValue(value);
            parameter.setLogicalDelIn("N");

            batchJobRepository.save(parameter);
        } else {
            parameter = new DefaultParameter(parameterId, value);
            batchJobRepository.save(parameter);
        }
        return convertDefaultParametersToDTOList(batchJobRepository.getBatchDefaultParameters(jobName));
    }

}
