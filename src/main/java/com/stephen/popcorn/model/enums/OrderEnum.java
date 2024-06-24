package com.stephen.popcorn.model.enums;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author stephen qiu
 */
public enum OrderEnum {
	
	SUCCESS("正常", 0),
	ERROR("异常", 1);
	
	private final String text;
	
	private final Integer value;
	
	OrderEnum(String text, Integer value) {
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
	public static OrderEnum getEnumByValue(Integer value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}
		for (OrderEnum anEnum : OrderEnum.values()) {
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
