package com.synctech.service;

import java.awt.image.BufferedImage;

import com.synctech.domian.MiaoshaUser;
import com.synctech.domian.OrderInfo;
import com.synctech.vo.GoodsVo;

public interface MiaoshaService {

	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo);

	long getMiaoshaResult(Long userId, Long goodsId);

	public String createMiaoshaPath(Long id, Long goodsId);

	public boolean getMiaoshaPath(Long id, Long goodsId, String path);

	public BufferedImage createVerifyCode(Long id, Long goodsId);

	boolean checkVerifyCode(Long userId, Long goodsId, String verifyCode);

}
