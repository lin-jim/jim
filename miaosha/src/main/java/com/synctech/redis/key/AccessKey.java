package com.synctech.redis.key;

public class AccessKey extends BaseKey{

	private AccessKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}
	public static AccessKey withExpire(int expireSeconds){
		return new AccessKey(5,"ac");
	}
}
