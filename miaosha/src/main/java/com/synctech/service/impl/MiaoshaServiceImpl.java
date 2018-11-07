package com.synctech.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synctech.domian.MiaoshaOrder;
import com.synctech.domian.MiaoshaUser;
import com.synctech.domian.OrderInfo;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.MiaoshaKey;
import com.synctech.service.GoodsVoService;
import com.synctech.service.MiaoshaService;
import com.synctech.service.OrderService;
import com.synctech.utils.MD5Util;
import com.synctech.utils.UUIDUtil;
import com.synctech.vo.GoodsVo;

@Service
public class MiaoshaServiceImpl implements MiaoshaService{

	@Autowired
	private GoodsVoService goodsVoService;
	
	@Autowired
	private OrderService orderSerivce;
	
	@Autowired
	private RedisUtils redisService;
	
	@Transactional
	@Override
	public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
		//减库存  
		boolean success = goodsVoService.reduce(goodsVo);
		if (success) {
			//下订单  写入秒杀订单
			return orderSerivce.createOrder(user,goodsVo);
		}else {//秒杀失败,
			setGoodsOver(goodsVo.getId());
			return null;
		}
		
	}


	@Override
	public long getMiaoshaResult(Long userId, Long goodsId) {
		MiaoshaOrder order = orderSerivce.getOrderInfoByUserIdGoodsId(userId, goodsId);
//		MiaoshaOrder order = redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId, MiaoshaOrder.class);
		System.out.println(order);
		if (order != null) {//秒杀到
			return order.getOrderId();
		}else {
			boolean isOver = getGoodsOver(goodsId);
			if (isOver) {
				return -1;
			}else{
				return 0;
			}
		}
	}

	//库存不足时标记为true
	private void setGoodsOver(Long goodsId) {
		redisService.set(MiaoshaKey.goodsOver, ""+goodsId, true);
	}
	
	//判断是否还有库存
	private boolean getGoodsOver(long goodsId) {
		boolean exists = redisService.exists(MiaoshaKey.goodsOver, ""+goodsId);
		System.out.println(exists);
		return exists;
	}


	@Override
	public String createMiaoshaPath(Long userId, Long goodsId) {
		String path = MD5Util.md5(UUIDUtil.uuid());
		redisService.set(MiaoshaKey.getMiaoshaPath, ""+userId+"_"+goodsId, path);
		return path;
	}


	@Override
	public boolean getMiaoshaPath(Long userId, Long goodsId, String path) {
		String oldPath = redisService.get(MiaoshaKey.getMiaoshaPath, ""+userId+"_"+goodsId, String.class);
		return path.equals(oldPath);
	}


	@Override
	public BufferedImage createVerifyCode(Long userId, Long goodsId) {
		int width = 80;
		int height = 32;
		//create the image
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		// set the background color
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);
		// draw the border
		g.setColor(Color.black);
		g.drawRect(0, 0, width - 1, height - 1);
		// create a random instance to generate the codes
		Random rdm = new Random();
		// make some confusion
		for (int i = 0; i < 50; i++) {
			int x = rdm.nextInt(width);
			int y = rdm.nextInt(height);
			g.drawOval(x, y, 0, 0);
		}
		// generate a random code
		String verifyCode = genarateVerifyCode(rdm);
		g.setColor(new Color(0, 100, 0));
		g.setFont(new Font("Candara", Font.BOLD, 24));
		g.drawString(verifyCode, 8, 24);
		g.dispose();
		//把验证码存到redis中
		int rnd = calc(verifyCode);
		redisService.set(MiaoshaKey.getVerifyCode, userId+","+goodsId, rnd);
		//输出图片	
		return image;
	}

	private static final char[] ops = {'+','-','*'};
	private String genarateVerifyCode(Random rdm) {
		int num1 = rdm.nextInt(10);
		int num2 = rdm.nextInt(10);
		int num3 = rdm.nextInt(10);
		char op1 = ops[rdm.nextInt(3)];
		char op2 = ops[rdm.nextInt(3)];
		String exp = ""+num1+op1+num2+op2+num3;
		return exp;
	}
	
	public static int calc(String exp) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");
		try {
			return (int) engine.eval(exp);
		} catch (ScriptException e) {
			e.printStackTrace();
			return 0;
		}
	
	}


	@Override
	public boolean checkVerifyCode(Long userId, Long goodsId,String verifyCode) {
		Integer oldVerifyCode = redisService.get(MiaoshaKey.getVerifyCode, ""+userId+","+goodsId, int.class);
		if (oldVerifyCode == null ||Integer.parseInt(verifyCode) - oldVerifyCode != 0) {
			return false;
		}
		redisService.del(MiaoshaKey.getVerifyCode, ""+userId+","+goodsId);
		return true;
	}


}
