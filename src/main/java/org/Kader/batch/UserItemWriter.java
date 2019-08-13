package org.Kader.batch;

import java.util.List;

import org.Kader.entities.User;
import org.Kader.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class UserItemWriter implements ItemWriter<User> {
	
	private static final Logger log = LoggerFactory.getLogger(UserItemWriter.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public void write(List<? extends User> users) throws Exception {
		userRepository.saveAll(users);
		log.info("{} users saved in Database :" +users.size());	
	}

}
