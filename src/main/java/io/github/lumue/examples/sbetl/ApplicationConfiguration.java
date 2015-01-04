package io.github.lumue.examples.sbetl;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@EnableBatchProcessing
@EnableTransactionManagement
public class ApplicationConfiguration {

	@Bean
	public ItemReader<Person> reader() {
		PersonCsvFileReader reader = new PersonCsvFileReader();
		reader.setResource(new ClassPathResource("person.csv"));
		return reader;
	}

	@Bean
	public ItemWriter<Person> writer(DataSource dataSource) {
		PersonWriter personWriter = new PersonWriter();
		personWriter.setDataSource(dataSource);
		return personWriter;
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Person> reader,
			ItemWriter<Person> writer) {
		return stepBuilderFactory.get("step1")
				.<Person, Person> chunk(10)
				.reader(reader)
				.writer(writer)
				.build();
	}

	@Bean
	public Job job(JobBuilderFactory jobs, Step step1) {
		return jobs.get("job")
				.incrementer(new RunIdIncrementer())
				.flow(step1)
				.end()
				.build();
	}

	public static void main(String[] args) throws Exception {
		System.exit(SpringApplication.exit(SpringApplication.run(
				ApplicationConfiguration.class, args)));
	}
}
