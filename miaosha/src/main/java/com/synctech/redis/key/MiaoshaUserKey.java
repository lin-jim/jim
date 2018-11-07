package com.synctech.redis.key;

public class MiaoshaUserKey extends BaseKey{

	public static final int TOKEN_EXPIRE_SECONDS = 3600*24*2;
	
	private MiaoshaUserKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}

	public static final MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE_SECONDS,"token");
	public static final MiaoshaUserKey getById = new MiaoshaUserKey(0,"getById");
}
