package com.synctech.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.synctech.result.CodeMsg;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value=Exception.class)
	public CodeMsg globalException(HttpServletRequest request,Exception e){
		if (e instanceof BindException) {
			BindException be = (BindException) e;
			List<ObjectError> errors = be.getAllErrors();
			ObjectError error = errors.get(0);
			String message = error.getDefaultMessage();
			return CodeMsg.BIND_ERROR.fillArgs(message);
		}else if (e instanceof GlobalExcption) {
			GlobalExcption ge = (GlobalExcption) e;
			return ge.getCm();
		}
		return null;
	}
}
