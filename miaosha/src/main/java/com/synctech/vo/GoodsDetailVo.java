package com.synctech.vo;

import com.synctech.domian.MiaoshaUser;

public class GoodsDetailVo {

	private MiaoshaUser user; 
	private GoodsVo goods;
	private int status;
	private int remainSeconds;
	public MiaoshaUser getUser() {
		return user;
	}
	public void setUser(MiaoshaUser user) {
		this.user = user;
	}
	public GoodsVo getGoods() {
		return goods;
	}
	public void setGoods(GoodsVo goods) {
		this.goods = goods;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRemainSeconds() {
		return remainSeconds;
	}
	public void setRemainSeconds(int remainSeconds) {
		this.remainSeconds = remainSeconds;
	}
	
}
