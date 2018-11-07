package com.synctech.access;

import com.synctech.domian.MiaoshaUser;

public class UserContext {

	public static ThreadLocal<MiaoshaUser> threadLocal = new ThreadLocal<>();
	
	public static void setUser(MiaoshaUser user){
		threadLocal.set(user);
	}
	
	public static MiaoshaUser getUser(){
		return threadLocal.get();
	}

}

