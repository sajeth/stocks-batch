/**
 *
 */
package com.saji.stocks.batch.services;

import com.saji.stocks.batch.dto.DefaultParameterDTO;
import com.saji.stocks.batch.services.model.Statuses;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author sajeth
 *
 */
@SuppressWarnings("exports")
@RestController
@RequestMapping("/quartz")
public class QuartzWebServiceImpl implements QuartzWebService {
    @Autowired
    private QuartzServiceTransactional service;

//    @Inject
//    public QuartzWebServiceImpl(final QuartzServiceTransactional service) {
//        this.service = service;
//    }

    @Override
    @RequestMapping(value = "/forceComplete/{jobName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> forceComplete(String jobName) {
        this.service.forceCompleteMostRecentRun(jobName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/run/{jobName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statuses> kickOffJob(@PathVariable final String jobName,
                                               @RequestBody Map<String, String> bodyRequest) throws SchedulerException {
        return new ResponseEntity<>(this.service.kickOffJob(jobName, bodyRequest), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statuses> retrieveStatuses() throws SchedulerException {
        return new ResponseEntity<>(this.service.retrieveStatuses(), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/pause/{jobName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statuses> pauseJob(@PathVariable String jobName) throws SchedulerException {
        return new ResponseEntity<>(this.service.pauseJob(jobName), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/resume/{jobName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statuses> resumeJob(@PathVariable String jobName) throws SchedulerException {
        return new ResponseEntity<>(this.service.resumeJob(jobName), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/kill/{jobName}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statuses> killJob(@PathVariable final String jobName) throws SchedulerException {
        return new ResponseEntity<>(this.service.killJob(jobName), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/default/{jobName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DefaultParameterDTO>> fetchParametersForJob(@PathVariable final String jobName) {
        return new ResponseEntity<>(this.service.fetchParametersForJob(jobName), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/default/{jobName}/{key}/{value}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DefaultParameterDTO>> saveParameterForJob(@PathVariable final String jobName,
                                                                         @PathVariable final String key, @PathVariable final String value) {
        return new ResponseEntity<>(this.service.saveParametersForJob(jobName, key, value), HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/reschedule/{jobName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statuses> rescheduleJob(String jobName, Map<String, String> bodyRequest)
            throws SchedulerException {
        Statuses body = null;
        try {
            body = this.service.rescheduleJob(jobName, bodyRequest);
        } catch (Exception e) {

        }
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.saji.stocks.batch.services.QuartzWebService#retrieveStatus(java.lang.
     * String)
     */
    @Override
    public ResponseEntity<Statuses> retrieveStatus(String jobName) throws SchedulerException {
        return new ResponseEntity<>(this.service.retrieveStatus(jobName), HttpStatus.OK);
    }

}
