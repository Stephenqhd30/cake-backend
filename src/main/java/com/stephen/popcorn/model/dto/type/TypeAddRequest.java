package com.stephen.popcorn.model.dto.type;

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
public class TypeAddRequest implements Serializable {
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