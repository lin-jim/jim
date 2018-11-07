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
		User u1 = new User();
		u1.setId(2);
		u1.setName("222");
		userDao.insert(u1);
		User u2 = new User();
		u2.setId(1);
		u2.setName("111");
		userDao.insert(u2);
	}

}
