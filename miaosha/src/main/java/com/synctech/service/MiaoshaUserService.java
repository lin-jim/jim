package com.synctech.service;

import javax.servlet.http.HttpServletResponse;

import com.synctech.domian.MiaoshaUser;
import com.synctech.vo.LoginVo;

public interface MiaoshaUserService {

	public MiaoshaUser getById(long id);

	public boolean login(HttpServletResponse response, LoginVo loginVo);

	public MiaoshaUser getByToken(String token, HttpServletResponse response);

	boolean updatePassword(String token, long id, String passwordNew);
}
