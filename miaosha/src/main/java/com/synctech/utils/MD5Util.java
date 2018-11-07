package com.synctech.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

	private static final String salt = "1A2B3C4D";
	
	public static String md5(String str){
		return DigestUtils.md5Hex(str);
	}
	
	/**
	 * 前台加密结果
	 * @param str
	 * @return
	 */
	public static String inputPwdToFormPwd(String str){
		String pwd = ""+salt.charAt(0)+salt.charAt(1)+str+salt.charAt(5)+salt.charAt(6);
		return md5(pwd);
	}
	
	/**
	 * 后台加密结果
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String formPwdToDBPwd(String str,String salt){
		String pwd = ""+salt.charAt(0)+salt.charAt(1)+str+salt.charAt(5)+salt.charAt(6);
		return md5(pwd);
	}
	
	/**
	 * 前台到后台的加密结果
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String inputPwdToDBPwd(String str,String salt){
		String inputPwdToFormPwd = inputPwdToFormPwd(str);
		String formPwdToDBPwd = formPwdToDBPwd(inputPwdToFormPwd,"1A2B3C4D");
		return formPwdToDBPwd;
	}
	
	public static void main(String[] args) {
//		System.out.println(inputPwdToFormPwd("123456"));//a60e5197d88539347e499173783eaeab
//		System.out.println(formPwdToDBPwd(inputPwdToFormPwd(""), salt));
//		System.out.println(inputPwdToDBPwd("123456", salt));
		int[] arr = {2,23,12,5,32,13};
		for (int i = 0; i < arr.length-1; i++) {
			for (int j = 0; j < arr.length-1-i; j++) {
				if (arr[j] > arr[j+1]) {
					int temp = arr[j];
					arr[j] = arr[j+1];
					arr[j+1] = temp;
				}
			}
		}
		for (int i : arr) {
			System.out.print(i+",");
		}
	}
}
