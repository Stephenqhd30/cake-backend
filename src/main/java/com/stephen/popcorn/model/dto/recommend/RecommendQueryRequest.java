package com.stephen.popcorn.model.dto.recommend;

import com.stephen.popcorn.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author stephen qiu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecommendQueryRequest extends PageRequest implements Serializable {
	
	/**
	 * 推荐栏id
	 */
	private Long id;
	
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