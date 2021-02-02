/**
 *
 */
package com.saji.stocks.batch.services;

import com.saji.stocks.batch.dto.DefaultParameterDTO;
import com.saji.stocks.batch.services.model.Statuses;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author sajeth
 *
 */
@SuppressWarnings("exports")
@Component
public interface QuartzServiceTransactional {
    void forceCompleteMostRecentRun(final String jobName);

    Statuses kickOffJob(final String jobName, final Map<String, String> environmentVariables)
            throws SchedulerException;

    Statuses retrieveStatuses() throws SchedulerException;

    Statuses retrieveStatus(final String jobName) throws SchedulerException;

    Statuses rescheduleJob(final String jobName, final Map<String, String> bodyRequest)
            throws SchedulerException, ParseException;

    Statuses pauseJob(final String jobName) throws SchedulerException;

    Statuses resumeJob(final String jobName) throws SchedulerException;

    Statuses killJob(final String jobName) throws SchedulerException;

    List<DefaultParameterDTO> fetchParametersForJob(final String jobName);

    List<DefaultParameterDTO> saveParametersForJob(final String jobName, final String key, final String value);

}
