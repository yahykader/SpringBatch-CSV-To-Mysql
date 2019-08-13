package org.Kader.config;

import javax.sql.DataSource;

import org.Kader.batch.UserItemProcessor;
import org.Kader.entities.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	public DataSource dataSource;
	
	@Qualifier(value="importUserJob")
	@Bean
	public Job importUserJob() throws Exception {
		return jobBuilderFactory.get("importUserJob")
				                .incrementer(new RunIdIncrementer())
				               // .listener(listener())
				                .start(step1())
				                .build();
	}
	
	@Bean
	public Step step1() throws Exception{
		return stepBuilderFactory.get("step1")
								 .<User,User>chunk(5)
								 .reader(reader())
								 .processor(processor())
								 .writer(writer())
								 .build();
		
	}

	@Bean
	@StepScope
	Resource inputFileRessource(@Value("#{jobParameters[fileName]}") final String fileName)  throws Exception{
		return  new ClassPathResource(fileName);
	}
	

	@Bean
	@StepScope
	public FlatFileItemReader<User> reader() throws Exception{
		FlatFileItemReader<User> reader=new FlatFileItemReader<>();
		reader.setResource(inputFileRessource(null));
		//reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<User>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames("id","firstName","lastName","email","age");
				//setNames(new String[] {"id","first_name","last_name","email","age"});
			}});
			setFieldSetMapper(new UserFileRowMapper());
		}});
		return reader;
	}

	/*private LineMapper<User> lineMapper() {
		DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
		DelimitedLineTokenizer lineTokenizer =new DelimitedLineTokenizer();
		
		
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setNames(new String[] {"name"});
		lineTokenizer.setStrict(false);
		
		BeanWrapperFieldSetMapper<User> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(User.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	*/
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<User> writer(){
		JdbcBatchItemWriter<User> writer=new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO user (id, first_name, last_name, email, age) VALUES (:id, :firstName, :lastName, :email, :age)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
		return writer;	
	}
	
	/*@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource db=new DriverManagerDataSource();
		db.setDriverClassName("com.mysql.cj.jdbc.Driver");
		db.setUrl("jdbc:mysql://localhost/loadcsv");
		db.setUsername("root");
		db.setPassword("root");
		return dataSource;
	}
	*/
}
