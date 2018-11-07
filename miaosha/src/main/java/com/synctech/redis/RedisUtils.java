package com.synctech.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.synctech.redis.key.KeyPrefix;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisUtils {

	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 获取key
	 * @param prefix
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> T get(KeyPrefix prefix,String key,Class<T> clazz){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;
			String val = jedis.get(realKey);
			T t = stringToBean(val,clazz);
			return t;
		} finally {
			returnToPool(jedis);
		}
		
	}

	/**
	 * 设置key
	 * @param prefix
	 * @param key
	 * @param val
	 * @return
	 */
	public <T> boolean set(KeyPrefix prefix,String key,T val){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String str = beanToString(val);
			if (str == null || str.length() <= 0) {
				return false;
			}
			String realKey = prefix.getPrefix()+key;
			if (prefix.expireSeconds() <= 0) {
				jedis.set(realKey, str);
			}else{
				jedis.setex(realKey, prefix.expireSeconds(), str);
			}
			return true;
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 判断key是否存在
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> boolean exists(KeyPrefix prefix,String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;
			return jedis.exists(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	
	public boolean del(KeyPrefix prefix,String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;
			Long del = jedis.del(realKey);
			return del > 0;
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 增加
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Long incr(KeyPrefix prefix,String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;
			return jedis.incr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	
	/**
	 * 减少
	 * @param prefix
	 * @param key
	 * @return
	 */
	public <T> Long decr(KeyPrefix prefix,String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix()+key;
			return jedis.decr(realKey);
		} finally {
			returnToPool(jedis);
		}
	}
	
	public static <T> String beanToString(T val) {
		if (val == null) {
			return null;
		}
		Class<?> clazz = val.getClass();
		if (clazz == int.class || clazz == Integer.class) {
			return ""+val;
		}else if (clazz == long.class || clazz == Long.class) {
			return ""+val;
		}else if (clazz == String.class) {
			return (String) val;
		}else {
			return JSON.toJSONString(val);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T stringToBean(String val,Class<T> clazz) {
		if (val == null || val.length() <= 0 || clazz == null) {
			return null;
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) Integer.valueOf(val);
		}else if (clazz == long.class || clazz == Long.class) {
			return (T) Long.valueOf(val);
		}else if (clazz == String.class) {
			return (T) val;
		}else {
			return JSONObject.toJavaObject(JSON.parseObject(val), clazz);
		}
	}

	private void returnToPool(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
		
	}
}
