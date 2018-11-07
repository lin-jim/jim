package com.synctech.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synctech.dao.OrderDao;
import com.synctech.domian.MiaoshaOrder;
import com.synctech.domian.MiaoshaUser;
import com.synctech.domian.OrderInfo;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.OrderKey;
import com.synctech.service.OrderService;
import com.synctech.vo.GoodsVo;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private RedisUtils redisService;
	
	@Override
	public MiaoshaOrder getOrderInfoByUserIdGoodsId(Long userId, Long goodsId) {
		MiaoshaOrder order = redisService.get(OrderKey.getMiaoshaOrderByUidGid, ""+userId+"_"+goodsId, MiaoshaOrder.class);
		if (order == null) {
			return null;
		}else{
			return order;
		}
	}

	@Transactional
	@Override
	public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goodsVo.getId());
		orderInfo.setGoodsName(goodsVo.getGoodsName());
		orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insertOrderInfo(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goodsVo.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(user.getId());
		orderDao.insertMiaoshaOrder(miaoshaOrder);
		redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goodsVo.getId(), miaoshaOrder);
		return orderInfo;
	}

	@Override
	public OrderInfo getOrderByOrderId(Long orderId) {
		return orderDao.getOrderByOrderId(orderId);
	}

}
