package com.synctech.redis.key;

public class OrderKey extends BaseKey{

	public static final OrderKey getMiaoshaOrderByUidGid = new OrderKey(30,"oug");
	
	private OrderKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}

}
