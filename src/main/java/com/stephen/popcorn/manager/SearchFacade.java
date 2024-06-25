package com.stephen.popcorn.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.datasources.DataSource;
import com.stephen.popcorn.datasources.DataSourceRegistry;
import com.stephen.popcorn.datasources.GoodsDataSource;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.goods.GoodsQueryRequest;
import com.stephen.popcorn.model.dto.search.SearchRequest;
import com.stephen.popcorn.model.dto.user.UserQueryRequest;

import com.stephen.popcorn.model.enums.GoodsTypeEnum;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.SearchVO;
import com.stephen.popcorn.model.vo.UserVO;

import com.stephen.popcorn.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author: stephen qiu
 * @create: 2024-06-13 16:01
 **/
@Component
@Slf4j
public class SearchFacade {
	
	@Resource
	private GoodsService goodsService;
	
	@Resource
	private GoodsDataSource goodsDataSource;
	
	@Resource
	private DataSourceRegistry dataSourceRegistry;
	
	/**
	 * 聚合搜索查询
	 *
	 * @param searchRequest
	 * @return
	 */
	public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		// 先对 type 进行判断
		String type = searchRequest.getType();
		String searchText = searchRequest.getSearchText();
		int current = searchRequest.getCurrent();
		int pageSize = searchRequest.getPageSize();
		GoodsTypeEnum searchTypeEnum = GoodsTypeEnum.getEnumByValue(type);
		ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
		// 如果 type 为空
		if (searchTypeEnum == null) {
			// 加入并发
			CompletableFuture<Page<GoodsVO>> goodsPageCompletableFuture = CompletableFuture.supplyAsync(() -> {
				GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
				goodsQueryRequest.setGoodsName(searchText);
				return goodsDataSource.doSearch(searchText, current, pageSize);
			});
			CompletableFuture.allOf(goodsPageCompletableFuture).join();
			Page<GoodsVO> goodsVOPage = null;
			try {
				goodsVOPage = goodsPageCompletableFuture.get();
			} catch (Exception e) {
				log.error("查询异常:{}", e);
				throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
			}
			SearchVO searchVO = new SearchVO();
			searchVO.setDateList(goodsVOPage.getRecords());
			return searchVO;
		} else {
			SearchVO searchVO = new SearchVO();
			DataSource<GoodsVO> dataSource = dataSourceRegistry.getDataSourceByType(type);
			Page<GoodsVO> page = dataSource.doSearch(searchText, current, pageSize);
			searchVO.setDateList(page.getRecords());
			return searchVO;
		}
	}
}
