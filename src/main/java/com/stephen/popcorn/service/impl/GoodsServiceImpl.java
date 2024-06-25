package com.stephen.popcorn.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.constants.CommonConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.mapper.GoodsMapper;
import com.stephen.popcorn.model.dto.goods.GoodsQueryRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.UserVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.UserService;
import com.stephen.popcorn.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author stephen qiu
 * @description 针对表【goods(商品表)】的数据库操作Service实现
 * @createDate 2024-06-24 17:35:30
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
		implements GoodsService {
	
	@Resource
	private UserService userService;
	
	@Override
	public void validGoods(Goods goods, boolean add) {
		if (goods == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String goodsName = goods.getGoodsName();
		Double price = goods.getPrice();
		String content = goods.getContent();
		Integer stock = goods.getStock();
		Integer typeId = goods.getTypeId();
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(goodsName), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (ObjectUtils.isNotEmpty(typeId)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品类别不能为空");
		}
		if (ObjectUtils.isNotEmpty(stock)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "库存量不能少与0");
		}
		if (StringUtils.isNotBlank(content) && content.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
		}
	}
	
	/**
	 * 获取查询包装类
	 *
	 * @param goodsQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<Goods> getQueryWrapper(GoodsQueryRequest goodsQueryRequest) {
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
		if (goodsQueryRequest == null) {
			return queryWrapper;
		}
		Long id = goodsQueryRequest.getId();
		String goodsName = goodsQueryRequest.getGoodsName();
		String content = goodsQueryRequest.getContent();
		Integer typeId = goodsQueryRequest.getTypeId();
		Long userId = goodsQueryRequest.getUserId();
		String searchText = goodsQueryRequest.getSearchText();
		String sortField = goodsQueryRequest.getSortField();
		String sortOrder = goodsQueryRequest.getSortOrder();
		
		// 拼接查询条件
		if (StringUtils.isNotBlank(searchText)) {
			queryWrapper.and(qw -> qw.like("goodsName", searchText).or().like("content", searchText));
		}
		queryWrapper.like(ObjectUtils.isNotEmpty(typeId), "typeId", typeId);
		queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
		queryWrapper.like(StringUtils.isNotBlank(goodsName), "goodsName", goodsName);
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
	
	@Override
	public GoodsVO getGoodsVO(Goods goods, HttpServletRequest request) {
		GoodsVO goodsVO = GoodsVO.objToVo(goods);
		// 1. 关联查询用户信息
		Long userId = goods.getUserId();
		User user = null;
		if (userId != null && userId > 0) {
			user = userService.getById(userId);
		}
		UserVO userVO = userService.getUserVO(user);
		goodsVO.setUserVO(userVO);
		return goodsVO;
	}
	
	@Override
	public Page<GoodsVO> getGoodsVOPage(Page<Goods> goodsPage, HttpServletRequest request) {
		List<Goods> goodsList = goodsPage.getRecords();
		Page<GoodsVO> goodsVOPage = new Page<>(goodsPage.getCurrent(), goodsPage.getSize(), goodsPage.getTotal());
		if (CollUtil.isEmpty(goodsList)) {
			return goodsVOPage;
		}
		// 1. 关联查询用户信息
		Set<Long> userIdSet = goodsList.stream().map(Goods::getUserId).collect(Collectors.toSet());
		Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
				.collect(Collectors.groupingBy(User::getId));
		// 填充信息
		List<GoodsVO> goodsVOList = goodsList.stream().map(post -> {
			GoodsVO goodsVO = GoodsVO.objToVo(post);
			Long userId = post.getUserId();
			User user = null;
			if (userIdUserListMap.containsKey(userId)) {
				user = userIdUserListMap.get(userId).get(0);
			}
			goodsVO.setUserVO(userService.getUserVO(user));
			return goodsVO;
		}).collect(Collectors.toList());
		goodsVOPage.setRecords(goodsVOList);
		return goodsVOPage;
	}
	
	@Override
	public Page<GoodsVO> listGoodsVOByPage(GoodsQueryRequest goodsQueryRequest) {
		int size = goodsQueryRequest.getPageSize();
		int current = goodsQueryRequest.getCurrent();
		Page<Goods> goodsPage = this.page(new Page<>(current, size),
				this.getQueryWrapper(goodsQueryRequest));
		Page<Goods> goodsVOPage = new Page<>(current, size, goodsPage.getTotal());
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		return this.getGoodsVOPage(goodsVOPage, request);
	}
	
}