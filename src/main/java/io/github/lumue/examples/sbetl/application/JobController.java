package io.github.lumue.examples.sbetl.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.launch.NoSuchJobInstanceException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * wraps a {@link JobOperator} and exports its methods via http api
 * 
 * @author lm
 *
 */
@Controller
@RequestMapping("/job")
public class JobController {

	private final JobOperator jobOperator;

	private final JobLauncher jobLauncher;

	private final JobRegistry jobRegistry;

	@Autowired(required = true)
	public JobController(JobOperator jobOperator, JobLauncher jobLauncher, JobRegistry jobRegistry) {
		this.jobOperator = jobOperator;
		this.jobLauncher = jobLauncher;
		this.jobRegistry = jobRegistry;
	}
	


	@ResponseBody
	@RequestMapping("/{jobName}/instances")
	public List<Long> getJobInstances(@PathVariable("jobName") String jobName) throws NoSuchJobException {
		return jobOperator.getJobInstances(jobName, 0, Integer.MAX_VALUE);
	}

	@ResponseBody
	@RequestMapping("/{jobName}/executions")
	public List<String> getExecutions(@PathVariable("jobName") String jobName) throws NoSuchJobInstanceException, NoSuchJobException {
		
		Map<Long,List<Long>> instanceExecutions=new HashMap<Long, List<Long>>();
		List<Long> jobInstances = jobOperator.getJobInstances(jobName, 0, Integer.MAX_VALUE);
		for (Long instanceId : jobInstances) {
			instanceExecutions.put(instanceId, jobOperator.getExecutions(instanceId));
		}

		List<String> executionSummaries = new ArrayList<String>();
		instanceExecutions.forEach((instanceId, executionIds) -> {
			executionIds.forEach((executionId) -> {
				try {
					executionSummaries.add(instanceId + ";" + jobOperator.getSummary(executionId));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		});
		
		return executionSummaries;
	}


	@ResponseBody
	@RequestMapping("/{jobName}/running")
	public Set<Long> getRunningExecutions(@PathVariable("jobName") String jobName) throws NoSuchJobException {
		return jobOperator.getRunningExecutions(jobName);
	}



	@ResponseBody
	@RequestMapping("/{jobName}/startInstance")
	public Long start(@PathVariable("jobName") String jobName) throws NoSuchJobException, JobInstanceAlreadyExistsException,
			JobParametersInvalidException {
		return jobOperator.start(jobName, "");
	}

	@ResponseBody
	@RequestMapping("/{jobName}/run")
	public JobExecution run(@PathVariable("jobName") String jobName) throws NoSuchJobException, JobInstanceAlreadyExistsException,
			JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
		Job job = jobRegistry.getJob(jobName);
		return jobLauncher.run(job, new JobParameters());
	}

	@ResponseBody
	@RequestMapping("/{jobName}/startNextInstance")
	public Long startNextInstance(@PathVariable("jobName") String jobName) throws NoSuchJobException, JobParametersNotFoundException,
			JobRestartException,
			JobExecutionAlreadyRunningException, JobInstanceAlreadyCompleteException, UnexpectedJobExecutionException,
			JobParametersInvalidException {
		return jobOperator.startNextInstance(jobName);
	}


	@ResponseBody
	@RequestMapping("/execution/{executionId}/summary")
	public String getSummary(long executionId) throws NoSuchJobExecutionException {
		return jobOperator.getSummary(executionId);
	}

	@ResponseBody
	@RequestMapping("/execution/{executionId}/stepExecutionSummary")
	public Map<Long, String> getStepExecutionSummaries(long executionId) throws NoSuchJobExecutionException {
		return jobOperator.getStepExecutionSummaries(executionId);
	}

	@ResponseBody
	@RequestMapping("/list")
	public Set<String> getJobNames() {
		return jobOperator.getJobNames();
	}

	
}
