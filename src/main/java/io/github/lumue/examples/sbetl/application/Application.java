package io.github.lumue.examples.sbetl.application;


import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;


@Import(ApplicationConfiguration.class)
public class Application extends SpringApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(
				Application.class, args);
	}

}
