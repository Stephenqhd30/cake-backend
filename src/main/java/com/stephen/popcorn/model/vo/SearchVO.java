package com.stephen.popcorn.model.vo;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

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
	private List<GoodsVO> dateList;
	
}
