package io.github.lumue.examples.sbetl.application;


import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@EnableBatchProcessing
@EnableTransactionManagement
@ImportResource("classpath:io/github/lumue/examples/sbetl/job/*.xml")
public class Application {


	public static void main(String[] args) throws Exception {
		System.exit(SpringApplication.exit(SpringApplication.run(
				Application.class, args)));
	}
}
