package com.stephen.popcorn.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author stephen qiu
 */
@Getter
public enum GoodsTypeEnum {
	ICE_CREAM("iceCream", "冰淇淋系列"),
	SNACK("snack", "零食系列"),
	CHILDREN("children", "儿童系列"),
	METHOD("method", "法式系列"),
	CLASSIC("classic", "经典系列"),
	FESTIVAL("festival", "节日系列"),
	NOT_AFFORD("notAfford", "买不起系列");
	
	
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
	
}
