package org.Kader.config;

import org.Kader.entities.User;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class UserFileRowMapper implements FieldSetMapper<User>{

	@Override
	public User mapFieldSet(FieldSet fieldSet) throws BindException {
		User user=new User();
		user.setId(fieldSet.readString("id"));
		user.setFirstName(fieldSet.readString("firstName"));
		user.setLastName(fieldSet.readString("lastName"));
		user.setEmail(fieldSet.readString("email"));
		try {
			user.setAge(fieldSet.readInt("age"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return user;
	}

}
