package com.stephen.popcorn.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.datasources.DataSource;
import com.stephen.popcorn.datasources.DataSourceRegistry;
import com.stephen.popcorn.datasources.GoodsDataSource;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.model.dto.goods.GoodsQueryRequest;
import com.stephen.popcorn.model.dto.search.SearchRequest;
import com.stephen.popcorn.model.enums.GoodsTypeEnum;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 聚合搜索查询
 *
 * @author stephen qiu
 * @create: 2024-06-13 16:01
 **/
@Component
@Slf4j
public class SearchFacade {
	
	@Resource
	private GoodsDataSource goodsDataSource;
	
	@Resource
	private DataSourceRegistry dataSourceRegistry;
	
	/**
	 * 聚合搜索查询
	 *
	 * @param searchRequest
	 * @param request
	 * @return
	 */
	public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		String type = searchRequest.getType();
		String searchText = searchRequest.getSearchText();
		int current = searchRequest.getCurrent();
		int pageSize = searchRequest.getPageSize();
		
		SearchVO searchVO = new SearchVO();
		if (type == null || type.isEmpty()) {
			try {
				GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
				goodsQueryRequest.setGoodsName(searchText);
				Page<GoodsVO> goodsVOPage = goodsDataSource.doSearch(searchText, null, current, pageSize);
				searchVO.setDateList(goodsVOPage);
			} catch (Exception e) {
				log.error("查询异常: {}", e.getMessage(), e);
				throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
			}
		} else {
			GoodsTypeEnum searchTypeEnum = GoodsTypeEnum.getEnumByValue(type);
			if (searchTypeEnum == null) {
				log.error("未知的 type 类型: {}", type);
				throw new BusinessException(ErrorCode.PARAMS_ERROR, "未知的 type 类型");
			}
			DataSource<GoodsVO> dataSource = dataSourceRegistry.getDataSourceByType(type);
			Page<GoodsVO> page = dataSource.doSearch(searchText, type, current, pageSize);
			searchVO.setDateList(page);
		}
		
		return searchVO;
	}
}
