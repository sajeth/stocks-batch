/**
 *
 */
package com.saji.stocks.batch.services;

import com.saji.stocks.batch.dto.DefaultParameterDTO;
import com.saji.stocks.batch.services.model.Statuses;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author sajeth
 *
 */
@SuppressWarnings("exports")
public interface QuartzWebService {
    ResponseEntity<String> forceComplete(final String jobName);

    ResponseEntity<Statuses> kickOffJob(final String jobName, final Map<String, String> bodyRequest)
            throws SchedulerException;

    ResponseEntity<Statuses> retrieveStatus(final String jobName) throws SchedulerException;

    ResponseEntity<Statuses> pauseJob(final String jobName) throws SchedulerException;

    ResponseEntity<Statuses> resumeJob(final String jobName) throws SchedulerException;

    ResponseEntity<Statuses> killJob(final String jobName) throws SchedulerException;

    ResponseEntity<List<DefaultParameterDTO>> fetchParametersForJob(final String jobName);

    ResponseEntity<List<DefaultParameterDTO>> saveParameterForJob(final String jobName, final String key,
                                                                  final String value);

    ResponseEntity<Statuses> rescheduleJob(final String jobName, final Map<String, String> bodyRequest)
            throws SchedulerException;

    ResponseEntity<Statuses> retrieveStatuses() throws SchedulerException;

}
