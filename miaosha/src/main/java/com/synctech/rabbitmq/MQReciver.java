package com.synctech.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synctech.domian.MiaoshaOrder;
import com.synctech.domian.MiaoshaUser;
import com.synctech.redis.RedisUtils;
import com.synctech.service.GoodsVoService;
import com.synctech.service.MiaoshaService;
import com.synctech.service.OrderService;
import com.synctech.vo.GoodsVo;

@Service
public class MQReciver {

	@Autowired
	private GoodsVoService goodsVoService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MiaoshaService miaoshaService;
	
	@RabbitListener(queues=RabbitMQConfig.MIAOSHA_QUEUE)
	public void reciveMiaoshaMessage(String miaoshaMessage){
		MiaoshaMessage mm = RedisUtils.stringToBean(miaoshaMessage, MiaoshaMessage.class);
		MiaoshaUser user = mm.getUser();
		long goodsId = mm.getGoodsId();
		GoodsVo goodsVo = goodsVoService.getGoodsVoByGoodsId(goodsId);
		if (goodsVo.getStockCount() < 0) {//判断库存
			return;
		}
		MiaoshaOrder order = orderService.getOrderInfoByUserIdGoodsId(user.getId(), goodsId);
		if (order != null) {//是否秒杀过
			return;
		}
		miaoshaService.miaosha(user, goodsVo);
	}
	
	/**
	 * 消费者
	 * @param message:接收到的消息
	 */
//	@RabbitListener(queues=RabbitMQConfig.QUEUE1)
//	public void reciveDirect1(String message){
//		System.out.println("[direct] info recive1 message:"+message);
//	}
//	@RabbitListener(queues=RabbitMQConfig.QUEUE2)
//	public void reciveDirect2(String message){
//		System.out.println("[direct] error recive2 message:"+message);
//	}
//	
////	@RabbitListener(queues=RabbitMQConfig.FANOUT_QUEUE1)
////	public void reciveFanout1(String message){
////		System.out.println("[fanout] recive1 message:"+message);
////	}
////	
////	@RabbitListener(queues=RabbitMQConfig.FANOUT_QUEUE2)
////	public void reciveFanout2(String message){
////		System.out.println("[fanout] recive2 message:"+message);
////	}
//	
//	@RabbitListener(queues=RabbitMQConfig.FANOUT_QUEUE1)
//	public void reciveTopic1(String message){
//		System.out.println("[topic] recive1 message:"+message);
//	}
//	
//	@RabbitListener(queues=RabbitMQConfig.FANOUT_QUEUE2)
//	public void reciveTopic2(String message){
//		System.out.println("[topic] recive2 message:"+message);
//	}
}
