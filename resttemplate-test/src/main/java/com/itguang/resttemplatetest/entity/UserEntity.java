package com.itguang.resttemplatetest.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 用户实体类
 *
 * @author itguang
 * @create 2017-12-16 9:44
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String username;

    private String password;

    private Integer age;

    private String email;


}
