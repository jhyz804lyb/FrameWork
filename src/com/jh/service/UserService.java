package com.jh.service;

import com.jh.base.BaseDao;
import com.jh.entity.User;

public interface UserService extends BaseDao<User>{
 public User addUser(User user);
}
