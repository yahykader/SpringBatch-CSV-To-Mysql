package org.Kader.batch;

import org.Kader.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
@Component
public class UserItemProcessor implements ItemProcessor<User, User>{
	
	
	private static final Logger log = LoggerFactory.getLogger(UserItemProcessor.class);


	@Override
	public User process(User user) throws Exception {
	    
		final String first_name=user.getFirstName().toUpperCase();
		final String last_name=user.getLastName().toUpperCase();
		final User transformUser=new User(user.getId(),first_name,last_name,user.getEmail(),user.getAge());
		
		log.info("Converting ( " +user+ " )into (" +transformUser+ ")");
		log.info("inside Processor :" +transformUser.toString());
		
		return transformUser;
	}

}
