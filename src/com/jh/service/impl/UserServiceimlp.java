package com.jh.service.impl;

import org.springframework.stereotype.Service;

import com.jh.base.BaseDaoImlp;
import com.jh.entity.User;
import com.jh.service.UserService;
@Service("userService")
public class UserServiceimlp extends BaseDaoImlp<User> implements UserService {

	
	public User addUser(User user) {
		try {
			this.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
