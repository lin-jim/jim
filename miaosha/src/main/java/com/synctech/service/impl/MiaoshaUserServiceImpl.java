package com.synctech.service.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synctech.dao.MiaoshaUserDao;
import com.synctech.domian.MiaoshaUser;
import com.synctech.exception.GlobalExcption;
import com.synctech.redis.RedisUtils;
import com.synctech.redis.key.MiaoshaUserKey;
import com.synctech.result.CodeMsg;
import com.synctech.service.MiaoshaUserService;
import com.synctech.utils.MD5Util;
import com.synctech.utils.UUIDUtil;
import com.synctech.vo.LoginVo;

@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService{

	public static final String MIAOSHA_TOKEN = "token";
	
	@Autowired
	private MiaoshaUserDao miaoshaUserDao;
	
	@Autowired
	private RedisUtils redisService;
	
	@Override
	public MiaoshaUser getById(long id) {
		//取缓存
		MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoshaUser.class);
		if (user != null) {
			return user;
		}
		//查询数据库
		user = miaoshaUserDao.getById(id);
		redisService.set(MiaoshaUserKey.getById, ""+id, user);
		return user;
	}

	
	
	@Override
	public boolean login(HttpServletResponse response,LoginVo loginVo) {
		if (loginVo == null) {
			throw new GlobalExcption(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String inputPwd = loginVo.getPassword();
		MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
		if (miaoshaUser == null) {
			throw new GlobalExcption(CodeMsg.MOBILE_NOT_EXISTS);
		}
		if (inputPwd.equals("36afa2c870dff708b1ef19b7e481280f")) {
			throw new GlobalExcption(CodeMsg.PASSWORD_EMPTY);
		}
		String formPwd = MD5Util.formPwdToDBPwd(inputPwd, miaoshaUser.getSalt());
		String password = miaoshaUser.getPassword();
		if (!password.equals(formPwd)) {
			throw new GlobalExcption(CodeMsg.PASSWORD_ERROR);
		}
		//存用户信息
		String token = UUIDUtil.uuid();
		addCookie(token, miaoshaUser, response);
		return true;
	}

	@Override
	public MiaoshaUser getByToken(String token,HttpServletResponse response) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		if (user != null) {//延长cookie的有效期
			addCookie(token, user, response);
		}
		return user;
	}

	public void addCookie(String token,MiaoshaUser miaoshaUser,HttpServletResponse response){
		redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
		Cookie cookie = new Cookie(MIAOSHA_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.TOKEN_EXPIRE_SECONDS);
		cookie.setPath("/");
		response.addCookie(cookie);
	}



	@Override
	public boolean updatePassword(String token,long id, String passwordNew) {
		MiaoshaUser user = getById(id);
		if (user == null) {
			throw new GlobalExcption(CodeMsg.MOBILE_NOT_EXISTS);
		}
		//更新数据库
		MiaoshaUser updatePassword = new MiaoshaUser();
		updatePassword.setId(id);
		updatePassword.setPassword(MD5Util.formPwdToDBPwd(passwordNew, updatePassword.getSalt()));
		miaoshaUserDao.updatePassword(updatePassword);
		//更新缓存
		redisService.del(MiaoshaUserKey.getById, ""+id);
		//更新token
		user.setPassword(updatePassword.getPassword());
		redisService.set(MiaoshaUserKey.token, token, user);
		return true;
	}
}
