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
public enum GoodsTypeEnum {
	
	USER("用户", "user"),
	ADMIN("管理员", "admin"),
	BAN("被封号", "ban");
	
	private final String text;
	
	private final String value;
	
	GoodsTypeEnum(String text, String value) {
		this.text = text;
		this.value = value;
	}
	
	/**
	 * 获取值列表
	 *
	 * @return
	 */
	public static List<String> getValues() {
		return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
	}
	
	/**
	 * 根据 value 获取枚举
	 *
	 * @param value
	 * @return
	 */
	public static GoodsTypeEnum getEnumByValue(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}
		for (GoodsTypeEnum anEnum : GoodsTypeEnum.values()) {
			if (anEnum.value.equals(value)) {
				return anEnum;
			}
		}
		return null;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
}
