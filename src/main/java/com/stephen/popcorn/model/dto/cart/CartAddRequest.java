package com.stephen.popcorn.model.dto.cart;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author stephen qiu
 */
@Data
public class CartAddRequest implements Serializable {
	
	/**
	 * 商品id
	 */
	private Long goodsId;
	
	/**
	 * 购买人id
	 */
	private Long userId;
	
	
	/**
	 * 购买数量
	 */
	private Integer quantity;
	
	
	
	private static final long serialVersionUID = 1L;
}