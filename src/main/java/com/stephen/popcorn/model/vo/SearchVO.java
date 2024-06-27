package com.stephen.popcorn.model.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: stephen qiu
 * @create: 2024-05-26 20:11
 **/
@Data
public class SearchVO implements Serializable {
	private static final long serialVersionUID = 9065946273183024389L;
	
	/**
	 * 列表
	 */
	private Page<GoodsVO> dateList;
	
}
