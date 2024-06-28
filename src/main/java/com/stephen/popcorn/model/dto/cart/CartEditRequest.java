package com.stephen.popcorn.model.dto.cart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 编辑请求
 *
 * @author stephen qiu
 */
@Data
public class CartEditRequest implements Serializable {
	
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