package com.synctech.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synctech.domian.User;
import com.synctech.rabbitmq.MQSender;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.UserKey;
import com.synctech.result.CodeMsg;
import com.synctech.result.Result;
import com.synctech.service.UserService;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RedisUtils redisUtils;
	
	@Autowired
	private MQSender sender;
	
//	@ResponseBody
//	@RequestMapping("/mq/topic")
//	public Result<String> topic(){
//		sender.topicFanout("hello world");
//		return Result.success("hello world");
//	}
//	
//	@ResponseBody
//	@RequestMapping("/mq/fanout")
//	public Result<String> fanout(){
//		sender.sendFanout("hello world");
//		return Result.success("hello world");
//	}
//	
//	@ResponseBody
//	@RequestMapping("/mq/direct")
//	public Result<String> direct(){
//		sender.sendDirect("this is msg");
//		return Result.success("hello world");
//	}
	
	@ResponseBody
	@RequestMapping("/hello")
	public String hello(){
		return "hello world";
	}
	
	@ResponseBody
	@RequestMapping("/success")
	public Result<String> success(){
		return Result.success("hello world");
	}
	
	@ResponseBody
	@RequestMapping("/error")
	public Result<String> error(){
		return Result.error(CodeMsg.SERVER_ERROR);
	}
	
	@RequestMapping("/thymeleaf")
	public String thymeleaf(Map<String, Object> map){
		map.put("name", "jim");
		return "thymeleaf";
	}
	
	@ResponseBody
	@RequestMapping("/db/getUserById/{id}")
	public Result<User> getUserById(@PathVariable("id") Integer id){
		User user = userService.getUserById(id);
		return Result.success(user);
	} 
	
	@ResponseBody
	@RequestMapping("/db/tx")
	public Result<Boolean> getTx(){
		userService.insert();
		return Result.success(true);
	} 
	
	@ResponseBody
	@RequestMapping("/get")
	public Result<String> get(){
		String string = redisUtils.get(UserKey.getUseById,""+1, String.class);
		return Result.success(string);
	} 
	
	@ResponseBody
	@RequestMapping("/set")
	public Result<Boolean> set(){
		User user = userService.getUserById(1);
		boolean set = redisUtils.set(UserKey.getUseById,""+1, user);
		return Result.success(set);
	} 
	
	@ResponseBody
	@RequestMapping("/exists")
	public Result<Boolean> exists(){
		boolean set = redisUtils.exists(UserKey.getUseById,""+1);
		return Result.success(set);
	} 
}

