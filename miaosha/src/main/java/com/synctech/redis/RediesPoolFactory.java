package com.synctech.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RediesPoolFactory {

	@Autowired 
	private RedisConfig redisConfig;
	
	@Bean
	public JedisPool getJedisPool(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		jedisPoolConfig.setTestOnBorrow(true);
		jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait()*1000);
		JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(),redisConfig.getTimeout()*1000,redisConfig.getPassword(),0);
		return jedisPool;
	}
	
}
