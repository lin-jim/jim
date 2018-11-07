package com.synctech.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.synctech.utils.ValidatorUtil;

public class ValidatorMobile implements ConstraintValidator<IsMobile, String>{

	@Override
	public void initialize(IsMobile constraintAnnotation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		return ValidatorUtil.isMobile(value);
	}

}
