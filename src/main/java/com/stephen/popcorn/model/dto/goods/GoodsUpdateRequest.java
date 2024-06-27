package com.stephen.popcorn.model.dto.goods;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *
 * @author stephen qiu
 */
@Data
public class GoodsUpdateRequest implements Serializable {
	
	/**
	 * 商品id
	 */
	private Long id;
	
	/**
	 * 商品名称
	 */
	private String goodsName;
	
	/**
	 * 商品封面图
	 */
	private String goodsCover;
	
	/**
	 * 商品详细图1
	 */
	private String goodsImage1;
	
	/**
	 * 商品详细图2
	 */
	private String goodsImage2;
	
	/**
	 * 商品价格
	 */
	private Double price;
	
	/**
	 * 商品介绍
	 */
	private String content;
	
	/**
	 * 商品库存
	 */
	private Integer stock;
	
	/**
	 * 商品类型
	 */
	private String typeName;
	
	private static final long serialVersionUID = 1L;
}