package com.synctech.redis.key;

public abstract class BaseKey implements KeyPrefix{
	
	private int expireSeconds;
	
	private String prefix;
	
	public BaseKey( String prefix) {//0代表永不过期
		this(0,prefix);
	}

	public BaseKey(int expireSeconds, String prefix) {
		this.expireSeconds = expireSeconds;
		this.prefix = prefix;
	}

	@Override
	public int expireSeconds() {
		return expireSeconds;
	}
	
	@Override
	public String getPrefix() {
		String name = getClass().getSimpleName();//获取类名拼接key
		return name+":"+prefix;
	}
}
