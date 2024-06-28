package com.stephen.popcorn.model.dto.cart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.stephen.popcorn.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author stephen qiu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartQueryRequest extends PageRequest implements Serializable {
	
	/**
	 * 购物车id
	 */
	private Long id;
	
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