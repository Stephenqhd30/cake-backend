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
	
	ALL("全部", "all"),
	ICE_CREAM("冰淇淋系列", "iceCream"),
	SNACK("零食系列", "snack"),
	CHILDREN("儿童系列", "children"),
	METHOD("法式系列", "method"),
	CLASSIC("经典系列", "classic"),
	FESTIVAL("节日系列", "festival"),
	NOT_AFFORD("买不起系列", "notAfford");
	
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
