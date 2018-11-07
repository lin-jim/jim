package com.synctech.redis.key;

public interface KeyPrefix {

	public int expireSeconds();
	
	public String getPrefix();
}
