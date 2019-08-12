package org.Kader.config;

import javax.sql.DataSource;

import org.Kader.batch.UserItemProcessor;
import org.Kader.entities.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javassist.ClassClassPath;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource db=new DriverManagerDataSource();
		db.setDriverClassName("com.mysql.jdbc.Driver");
		db.setUrl("jdbc:mysql://localhost:3306/loadcsv");
		db.setUsername("root");
		db.setPassword("root");
		return dataSource;
	}
	
	public FlatFileItemReader<User> reader(){
		FlatFileItemReader<User> reader=new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("users.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());
		return reader;
	}

	private LineMapper<User> lineMapper() {
		DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer =new DelimitedLineTokenizer();
		
		
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] {"id","name","dept","salary"});
		lineTokenizer.setStrict(false);
		
		BeanWrapperFieldSetMapper<User> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(User.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<User> writer(){
		JdbcBatchItemWriter<User> writer=new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
		writer.setSql("INSERT INTO user (id,name,dept,salary) VALUES (:id,:name,:dept,:salary)");
		writer.setDataSource(dataSource);
		
		return writer;	
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("load-CSV")
								 .<User,User>chunk(100)
								 .reader(reader())
								 .processor(processor())
								 .writer(writer())
								 .build();
		
	}
	
	@Bean
	public Job job() {
		return jobBuilderFactory.get("import User Job")
				                .incrementer(new RunIdIncrementer())
				                .flow(step1())
				                .end()
				                .build();
	}
	
	
	
	
	
	
	
	
	
	

}
