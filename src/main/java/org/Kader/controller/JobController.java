package org.Kader.controller;

import org.Kader.batch.JobRunner;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/run")
public class JobController {
	
	@Autowired
	private JobRunner jobRunner;
   
	@RequestMapping(value="/job")
	public String runJob() throws JobExecutionAlreadyRunningException, JobRestartException, 
	                              JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		jobRunner.runBatchjob();
		return String.format("Job importUserJob submitted successuflly");
		
	} 
}
