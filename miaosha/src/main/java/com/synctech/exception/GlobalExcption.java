package com.synctech.exception;

import com.synctech.result.CodeMsg;

public class GlobalExcption extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CodeMsg cm;
	
	public GlobalExcption (CodeMsg cm){
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}
	
}
