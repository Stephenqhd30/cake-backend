package com.stephen.popcorn.model.dto.orderItem;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author stephen qiu
 */
@Data
public class OrderItemAddRequest implements Serializable {
	
	/**
	 * 商品价格
	 */
	private Double goodsPrice;
	
	/**
	 * 商品数量
	 */
	private Integer goodsAmount;
	
	/**
	 * 商品id
	 */
	private Long goodsId;
	
	/**
	 * 订单id
	 */
	private Long orderId;
	
	
	
	private static final long serialVersionUID = 1L;
}