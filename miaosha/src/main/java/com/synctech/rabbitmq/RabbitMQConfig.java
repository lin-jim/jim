package com.synctech.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String MIAOSHA_QUEUE = "miaosha.queue";
	public static final String QUEUE1 = "direct.queue1";
	public static final String QUEUE2 = "direct.queue2";
	public static final String DIRECT_EXCHANGE = "direct.exchange";
	public static final String FANOUT_QUEUE1 = "fanout.queue1";
	public static final String FANOUT_QUEUE2 = "fanout.queue2";
	public static final String FANOUT_EXCHANGE = "fanount.exchange";
	public static final String TOPIC_EXCHANGE = "topic.exchange";
	/**
	 * 声明一个队列
	 * @return
	 */
	
	@Bean
	public Queue miaoshaQueue(){
		return new Queue(MIAOSHA_QUEUE, true);
	}
	
	@Bean
	public Queue queue1(){
		return new Queue(QUEUE1, true);
	}
	@Bean
	public Queue queue2(){
		return new Queue(QUEUE2, true);
	}
	
	@Bean
	public DirectExchange directExchange(){
		return new DirectExchange(DIRECT_EXCHANGE);
	}
	
	@Bean
	public Binding directBind1(){
		return BindingBuilder.bind(queue1()).to(directExchange()).with("info");
	}
	
	@Bean
	public Binding directBind2(){
		return BindingBuilder.bind(queue2()).to(directExchange()).with("error");
	}
	
	@Bean
	public Queue fanoutQueue1(){
		return new Queue(FANOUT_QUEUE1,true);
	}
	@Bean
	public Queue fanoutQueue2(){
		return new Queue(FANOUT_QUEUE2,true);
	}
	
	@Bean
	public FanoutExchange fanoutExchange(){
		return new FanoutExchange(FANOUT_EXCHANGE);
	}
	
	@Bean
	public Binding fanoutBind1(){
		return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
	}
	
	@Bean
	public Binding fanoutBind2(){
		return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
	}
	
	@Bean
	public TopicExchange topicExchange(){
		return new TopicExchange(TOPIC_EXCHANGE);
	}
	
	@Bean
	public Binding topicBind1(){
		return BindingBuilder.bind(fanoutQueue1()).to(topicExchange()).with("goods.list");
	}
	
	@Bean
	public Binding topicBind2(){
		return BindingBuilder.bind(fanoutQueue2()).to(topicExchange()).with("goods.#");
	}
}
