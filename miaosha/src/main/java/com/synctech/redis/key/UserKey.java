package com.synctech.redis.key;

public class UserKey extends BaseKey{

	public static final UserKey getUseById = new UserKey("id");
	
	private UserKey(String prefix) {
		super(0,prefix);
	}

}
