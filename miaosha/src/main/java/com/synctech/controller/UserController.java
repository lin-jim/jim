package com.synctech.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synctech.domian.MiaoshaUser;
import com.synctech.result.Result;

@Controller
@RequestMapping("/user")
public class UserController {

	@ResponseBody
	@RequestMapping("/info")
	public Result<MiaoshaUser> login(Model model,MiaoshaUser user){
		return Result.success(user);
	}
}
