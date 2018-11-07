package com.synctech.redis.key;

public class GoodsKey extends BaseKey{

	public static final GoodsKey getGoodsList = new GoodsKey(5,"gl");
	public static final GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
	public static final GoodsKey getGoodsStock = new GoodsKey(0,"gs");
	
	private GoodsKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}

}
