package io.github.lumue.examples.sbetl;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

public class PersonWriter extends JdbcBatchItemWriter<Person> {
	{
		setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
		setSql("INSERT INTO person (first_name, last_name,street,city,country,email) VALUES (:firstName, :lastName,:street,:city,:country,:email)");
	}
}
