package com.stephen.popcorn.model.dto.orderItem;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 * @author stephen qiu
 */
@Data
public class OrderItemUpdateRequest implements Serializable {
	
	/**
	 * 订单id
	 */
	private Long id;
	
	/**
	 * 商品价格
	 */
	private Double goodsPrice;
	
	/**
	 * 商品数量
	 */
	private Integer goodsAmount;
	
	private static final long serialVersionUID = 1L;
}