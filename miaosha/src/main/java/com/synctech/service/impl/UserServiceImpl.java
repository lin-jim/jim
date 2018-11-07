package com.synctech.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synctech.dao.UserDao;
import com.synctech.domian.User;
import com.synctech.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	
	@Autowired
	private UserDao userDao;
	
	@Override
	public User getUserById(Integer id) {
		return userDao.getUserById(id);
	}

	@Transactional
	@Override
	public void insert() {
		User u1 = User.builder().id(1).name("1111").build();
		userDao.insert(u1);
		User u2 = User.builder().id(2).name("2222").build();
		userDao.insert(u2);
	}

}
