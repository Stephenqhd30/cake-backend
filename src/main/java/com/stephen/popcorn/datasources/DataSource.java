package com.stephen.popcorn.datasources;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.model.dto.search.SearchRequest;
import com.stephen.popcorn.model.vo.GoodsVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 数据源接口（需要接入的数据源必须实现）
 * @author: stephen qiu
 * @create: 2024-06-13 16:38
 **/
public interface DataSource<T> {
	
	/**
	 * 搜索
	 *
	 * @param searchText
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
