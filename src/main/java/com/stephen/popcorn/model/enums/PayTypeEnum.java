package com.stephen.popcorn.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author stephen qiu
 */
public enum PayTypeEnum {
	
	WX("微信支付", 0),
	API("支付宝支付", 1);
	
	private final String text;
	
	private final Integer value;
	
	PayTypeEnum(String text, Integer value) {
		this.text = text;
		this.value = value;
	}
	
	/**
	 * 获取值列表
	 *
	 * @return
	 */
	public static List<Integer> getValues() {
		return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
	}
	
	/**
	 * 根据 value 获取枚举
	 *
	 * @param value
	 * @return
	 */
	public static PayTypeEnum getEnumByValue(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}
		for (PayTypeEnum anEnum : PayTypeEnum.values()) {
			if (anEnum.value.equals(value)) {
				return anEnum;
			}
		}
		return null;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
}
