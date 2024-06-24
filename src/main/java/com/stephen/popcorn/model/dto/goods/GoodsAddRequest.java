package com.stephen.popcorn.model.dto.goods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *
 * @author stephen qiu
 */
@Data
public class GoodsAddRequest implements Serializable {
	
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
	private Integer typeId;
	
	/**
	 * 创建人id
	 */
	private Long userId;
	
	private static final long serialVersionUID = 1L;
}