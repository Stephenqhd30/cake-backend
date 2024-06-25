package com.stephen.popcorn.model.dto.type;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑请求
 *
 * @author stephen qiu
 */
@Data
public class TypeEditRequest implements Serializable {
	
	/**
	 * 商品id
	 */
	private Long id;
	
	/**
	 * 商品名称
	 */
	private String typeName;
	
	
	private static final long serialVersionUID = 1L;
}