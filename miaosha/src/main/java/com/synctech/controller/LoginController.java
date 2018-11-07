package com.synctech.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synctech.result.Result;
import com.synctech.service.MiaoshaUserService;
import com.synctech.vo.LoginVo;

@Controller
@RequestMapping("/user")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private MiaoshaUserService miaoshaUserService;
	
	@RequestMapping("/login")
	public String login(){
		return "login";
	}
	
	@ResponseBody
	@RequestMapping("/doLogin")
	public Result<Boolean> doLogin(HttpServletResponse response ,@Valid LoginVo loginVo){
		log.info(loginVo.toString());
//		String mobile = (String) loginVo.getMobile();
//		String inputPwd = loginVo.getPassword();
//		if (StringUtils.isEmpty(inputPwd)) {
//			return Result.error(CodeMsg.PASSWORD_EMPTY);
//		}
//		if (StringUtils.isEmpty(mobile)) {
//			return Result.error(CodeMsg.MOBILE_EMPTY);
//		}
//		if (!ValidatorUtils.isMobile(mobile)) {
//			return Result.error(CodeMsg.MOBILE_ERROR);
//		}
		//登录操作
		miaoshaUserService.login(response,loginVo);
		return Result.success(true);
	}
}
