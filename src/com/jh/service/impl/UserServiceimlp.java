package com.jh.service.impl;

import com.jh.utils.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Service;

import com.jh.base.BaseDaoImlp;
import com.jh.entity.User;
import com.jh.service.UserService;

import java.util.List;

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

	@Override
	public boolean checkUserName(String userName)
	{
		if(StringUtils.isNull(userName)) return true;
		Query query = this.createQuery("from User u where u.username =:username");
		query.setParameter("username",userName);
		List list = query.list();
		return list==null||list.size()==0;
	}

}
