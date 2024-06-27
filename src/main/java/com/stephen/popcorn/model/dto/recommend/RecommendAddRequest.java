package com.stephen.popcorn.model.dto.recommend;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author stephen qiu
 */
@Data
public class RecommendAddRequest implements Serializable {
	
	/**
	 * 商品类别
	 */
	private String goodsType;
	
	/**
	 * 商品id
	 */
	private Integer goodsId;
	
	
	private static final long serialVersionUID = 1L;
}