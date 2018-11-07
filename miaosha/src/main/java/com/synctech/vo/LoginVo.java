package com.synctech.vo;

import javax.validation.GroupSequence;

import org.hibernate.validator.constraints.NotEmpty;

import com.synctech.validator.IsMobile;
import com.synctech.vo.implement.First;
import com.synctech.vo.implement.Second;
import com.synctech.vo.implement.Third;
@GroupSequence(value={First.class,Second.class,Third.class,LoginVo.class})
public class LoginVo {

	@NotEmpty(message="手机号不能为空",groups=First.class)
	@IsMobile(groups=Second.class)
	private String mobile;
	private String password;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
	}
	
}
