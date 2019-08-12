package org.Kader.batch;

import org.Kader.entities.User;
import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User users) throws Exception {
		// TODO Auto-generated method stub
		return users;
	}

}
