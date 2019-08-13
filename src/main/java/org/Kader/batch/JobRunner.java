package org.Kader.batch;

import java.util.Date;
import java.util.Properties;

import javax.batch.operations.JobExecutionAlreadyCompleteException;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;

import org.Kader.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class JobRunner {

	private static final Logger log = LoggerFactory.getLogger(JobRunner.class);
	
	@Autowired
	private JobLauncher simpleJobLauncher;
	@Autowired
	private Job importUserJob;
	
	@Async
	public void runBatchjob() throws JobExecutionAlreadyRunningException, JobRestartException, 
	                                 JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		JobParametersBuilder jobParametersBuilder=new JobParametersBuilder();
		jobParametersBuilder.addString(Constants.FILE_NAME_CONTEXT_KEY, "users.csv");
		jobParametersBuilder.addDate("date", new Date(), true);
		runJob(importUserJob, jobParametersBuilder.toJobParameters());	
	}
	
	
	public void runJob(Job job,JobParameters parameters) throws JobExecutionAlreadyRunningException, JobRestartException, 
	                                                            JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		try {
			org.springframework.batch.core.JobExecution jobExecution= simpleJobLauncher.run(job, parameters);

		} catch (JobExecutionAlreadyCompleteException e) {
		    log.info("Job with fileName={} is already Running " +parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));

		} catch (JobRestartException e) {
		    log.info("Job with fileName={} is was not Restarted " +parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
	
		} catch (JobInstanceAlreadyCompleteException e) {
		    log.info("Job with fileName={} already Completed " +parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
		
		} catch (JobParametersInvalidException e) {
		    log.info("Invalid Job Parameters " +parameters.getParameters().get(Constants.FILE_NAME_CONTEXT_KEY));
		}
		
	}


}
