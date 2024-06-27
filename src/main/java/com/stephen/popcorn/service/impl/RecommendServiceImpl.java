package com.stephen.popcorn.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.constants.CommonConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.recommend.RecommendQueryRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.entity.Recommend;
import com.stephen.popcorn.model.enums.GoodsTypeEnum;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.RecommendVO;
import com.stephen.popcorn.model.vo.OrderVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.OrderService;
import com.stephen.popcorn.service.RecommendService;
import com.stephen.popcorn.mapper.RecommendMapper;
import com.stephen.popcorn.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author stephen qiu
* @description 针对表【recommend(推荐表)】的数据库操作Service实现
* @createDate 2024-06-24 17:35:21
*/
@Service
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend>
    implements RecommendService{
	
	@Resource
	private GoodsService goodsService;
	
	@Resource
	private OrderService orderService;
	
	@Override
	public void validRecommend(Recommend recommend, boolean add) {
		if (recommend == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String goodsType = recommend.getGoodsType();
		Long goodsId = recommend.getGoodsId();
		
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(ObjectUtils.isNotEmpty(goodsId), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (ObjectUtils.isNotEmpty(goodsType) && GoodsTypeEnum.getEnumByValue(goodsType).getValue() != null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品类型有误");
		}
	}
	
	/**
	 * 获取查询包装类
	 *
	 * @param recommendQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<Recommend> getQueryWrapper(RecommendQueryRequest recommendQueryRequest) {
		QueryWrapper<Recommend> queryWrapper = new QueryWrapper<>();
		if (recommendQueryRequest == null) {
			return queryWrapper;
		}
		Long id = recommendQueryRequest.getId();
		String goodsType = recommendQueryRequest.getGoodsType();
		Integer goodsId = recommendQueryRequest.getGoodsId();
		String sortField = recommendQueryRequest.getSortField();
		String sortOrder = recommendQueryRequest.getSortOrder();
		
		
		queryWrapper.eq(ObjectUtils.isNotEmpty(goodsType), "goodsType", goodsType);
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(goodsId), "goodsId", goodsId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
	
	@Override
	public RecommendVO getRecommendVO(Recommend recommend, HttpServletRequest request) {
		RecommendVO recommendVO = RecommendVO.objToVo(recommend);
		// 1. 关联查询信息
		Long goodsId = recommend.getGoodsId();
		Goods goods = null;
		if (goodsId != null && goodsId > 0) {
			goods = goodsService.getById(goodsId);
		}
		GoodsVO goodsVO = goodsService.getGoodsVO(goods, request);
		recommendVO.setGoodsVO(goodsVO);
		return recommendVO;
	}
	
	@Override
	public Page<RecommendVO> getRecommendVOPage(Page<Recommend> recommendPage, HttpServletRequest request) {
		List<Recommend> recommendList = recommendPage.getRecords();
		Page<RecommendVO> recommendVOPage = new Page<>(recommendPage.getCurrent(), recommendPage.getSize(), recommendPage.getTotal());
		if (CollUtil.isEmpty(recommendList)) {
			return recommendVOPage;
		}
		// 1. 关联查询信息
		Set<Long> goodsIdSet = recommendList.stream().map(Recommend::getGoodsId).collect(Collectors.toSet());
		Map<Long, List<Goods>> goodsIdGoodsListMap = goodsService.listByIds(goodsIdSet).stream()
				.collect(Collectors.groupingBy(Goods::getId));
		// 填充信息
		List<RecommendVO> recommendVOList = recommendList.stream().map(recommend -> {
			RecommendVO recommendVO = RecommendVO.objToVo(recommend);
			Long goodsId = recommend.getGoodsId();
			Goods goods = null;
			if (goodsIdGoodsListMap.containsKey(goodsId)) {
				goods = goodsIdGoodsListMap.get(goodsId).get(0);
			}
			recommendVO.setGoodsVO(goodsService.getGoodsVO(goods, request));
			return recommendVO;
		}).collect(Collectors.toList());
		recommendVOPage.setRecords(recommendVOList);
		return recommendVOPage;
	}
}




