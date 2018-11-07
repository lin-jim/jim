package com.synctech.redis.key;

public class MiaoshaKey extends BaseKey{

	public static final MiaoshaKey goodsOver = new MiaoshaKey(0,"go");
	
	public static final MiaoshaKey getMiaoshaPath = new MiaoshaKey(60,"mp");
	
	public static final MiaoshaKey getVerifyCode = new MiaoshaKey(180,"vc");
	
	private MiaoshaKey(int exprieSeconds,String prefix) {
		super(exprieSeconds,prefix);
	}

}
