package com.synctech.service;

import com.synctech.domian.MiaoshaOrder;
import com.synctech.domian.MiaoshaUser;
import com.synctech.domian.OrderInfo;
import com.synctech.vo.GoodsVo;

public interface OrderService {

	public MiaoshaOrder getOrderInfoByUserIdGoodsId(Long id, Long goodsId);

	public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo);

	OrderInfo getOrderByOrderId(Long orderId);

}
