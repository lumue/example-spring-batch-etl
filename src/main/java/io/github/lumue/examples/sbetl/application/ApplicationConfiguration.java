package io.github.lumue.examples.sbetl.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@EnableTransactionManagement
@ComponentScan(basePackages = "io.github.lumue")
@ImportResource({ "classpath:io/github/lumue/examples/sbetl/job/*.xml", "classpath:io/github/lumue/examples/sbetl/application/*.xml" })
public class ApplicationConfiguration {

	@Bean
	Path fileUploadPath(@Value("${application.upload.path}") String uploadPath) throws IOException {
		Path path = Files.createDirectories(Paths.get(uploadPath));
		return path;
	}

	@Bean
	public JobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
		simpleJobLauncher.setJobRepository(jobRepository);
		simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return simpleJobLauncher;
	}


}
