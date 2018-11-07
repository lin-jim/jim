package com.synctech.access;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.synctech.domian.MiaoshaUser;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.AccessKey;
import com.synctech.result.CodeMsg;
import com.synctech.service.MiaoshaUserService;
import com.synctech.service.impl.MiaoshaUserServiceImpl;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private MiaoshaUserService userService;
	
	@Autowired
	private RedisUtils redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = (HandlerMethod)handler;
			MiaoshaUser user = getMiaoshaUser(request,response);
			UserContext.setUser(user);
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if (needLogin) {
				if (user == null) {
					render(response, CodeMsg.SESSION_ERROR);
					return false;
				}
				 key = key+"_"+user.getId();
			}else {
				
			}
			AccessKey accessKey = AccessKey.withExpire(seconds);
			Integer count = redisService.get(accessKey, key, Integer.class);
			if (count == null) {
				redisService.set(accessKey, key, 1);
			}else if (count < maxCount) {
				count++;
				redisService.incr(accessKey, key);
			}else {
				render(response,CodeMsg.ACCESS_LIMIT_REACHED);
				return false;
			}
		}
		return true;
	}

	private void render(HttpServletResponse response,CodeMsg cm) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String code = JSON.toJSONString(cm);
		out.write(code.getBytes("UTF-8"));
		out.flush();
		out.close();
		
	}

	private MiaoshaUser getMiaoshaUser(HttpServletRequest request, HttpServletResponse response) {
		String paramToken = request.getParameter(MiaoshaUserServiceImpl.MIAOSHA_TOKEN);
		String cookieToken = getCookieValue(request,MiaoshaUserServiceImpl.MIAOSHA_TOKEN);
		if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(token, response);
	}
	
	private String getCookieValue(HttpServletRequest request, String token) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(token)) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
