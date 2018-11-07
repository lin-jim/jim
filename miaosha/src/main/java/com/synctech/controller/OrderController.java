package com.synctech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synctech.domian.MiaoshaUser;
import com.synctech.domian.OrderInfo;
import com.synctech.result.CodeMsg;
import com.synctech.result.Result;
import com.synctech.service.GoodsVoService;
import com.synctech.service.OrderService;
import com.synctech.vo.GoodsVo;
import com.synctech.vo.OrderDetailVo;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private GoodsVoService goodsService;
	
	@ResponseBody
	@RequestMapping("/showOrderDetail/{orderId}")
	public Result<OrderDetailVo> showOrderDetail(@PathVariable("orderId")Long orderId,MiaoshaUser user){
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		OrderInfo orderInfo = orderService.getOrderByOrderId(orderId);
		if (orderInfo == null) {
			return Result.error(CodeMsg.ORDER_NOT_EXISTS);
		}
		Long goodsId = orderInfo.getGoodsId();
		GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
		OrderDetailVo vo = new OrderDetailVo();
		vo.setGoods(goods);
		vo.setOrderInfo(orderInfo);
		return Result.success(vo);
	}
}
