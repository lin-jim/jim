package com.synctech.service;

import com.synctech.domian.User;

public interface UserService {

	public User getUserById(Integer id);
	
	public void insert();
}
