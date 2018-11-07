package com.synctech.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synctech.redis.RedisUtils;

@Service
public class MQSender {

	@Autowired
	private AmqpTemplate amqpTemplate;
	
	public void sendMiaoshaMessage(MiaoshaMessage mm) {
		String miaoshaMessage = RedisUtils.beanToString(mm);
		System.out.println(miaoshaMessage);
		amqpTemplate.convertAndSend(RabbitMQConfig.MIAOSHA_QUEUE,miaoshaMessage);
	}
	
	/**
	 * 生产者
	 * @param message:发送的消息
	 */
	/*public void sendDirect(Object message){
		String msg = RedisUtils.beanToString(message);
		System.out.println("[direct] sender message:"+msg);
		amqpTemplate.convertSendAndReceive(RabbitMQConfig.DIRECT_EXCHANGE,"warn" ,msg);
		amqpTemplate.convertSendAndReceive(RabbitMQConfig.DIRECT_EXCHANGE,"error" ,msg);
	}
	
	public void sendFanout(Object message){
		String msg = RedisUtils.beanToString(message);
		System.out.println("sender message:"+msg);
		amqpTemplate.convertSendAndReceive(RabbitMQConfig.FANOUT_EXCHANGE,"", msg);
	}
	
	public void topicFanout(Object message){
		String msg = RedisUtils.beanToString(message);
		System.out.println("sender message:"+msg);
		amqpTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE,"goods.list", msg);
		amqpTemplate.convertSendAndReceive(RabbitMQConfig.TOPIC_EXCHANGE,"goods.detail", msg);
	}
*/
	
}
