package com.stephen.popcorn.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.constants.CommonConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.mapper.CartMapper;
import com.stephen.popcorn.model.dto.cart.CartQueryRequest;
import com.stephen.popcorn.model.entity.Cart;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.CartVO;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.service.CartService;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.UserService;
import com.stephen.popcorn.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author stephen qiu
 * @description 针对表【cart(推荐表)】的数据库操作Service实现
 * @createDate 2024-06-24 17:35:21
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
		implements CartService {
	
	@Resource
	private GoodsService goodsService;
	
	@Resource
	private UserService userService;
	
	
	@Override
	public void validCart(Cart cart, boolean add) {
		if (cart == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Long goodsId = cart.getGoodsId();
		Long userId = cart.getUserId();
		Integer quantity = cart.getQuantity();
		
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(ObjectUtils.isNotEmpty(goodsId), ErrorCode.PARAMS_ERROR);
			ThrowUtils.throwIf(ObjectUtils.isNotEmpty(userId), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (ObjectUtils.isNotEmpty(quantity) && quantity < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "购买数量有误");
		}
	}
	
	/**
	 * 获取查询包装类
	 *
	 * @param cartQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<Cart> getQueryWrapper(CartQueryRequest cartQueryRequest) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		if (cartQueryRequest == null) {
			return queryWrapper;
		}
		Long id = cartQueryRequest.getId();
		Long goodsId = cartQueryRequest.getGoodsId();
		Long userId = cartQueryRequest.getUserId();
		Integer quantity = cartQueryRequest.getQuantity();
		String sortField = cartQueryRequest.getSortField();
		String sortOrder = cartQueryRequest.getSortOrder();
		
		
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(quantity), "quantity", quantity);
		queryWrapper.eq(ObjectUtils.isNotEmpty(goodsId), "goodsId", goodsId);
		queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
	
	@Override
	public CartVO getCartVO(Cart cart, HttpServletRequest request) {
		CartVO cartVO = CartVO.objToVo(cart);
		// 1. 关联查询信息
		Long goodsId = cart.getGoodsId();
		Goods goods = null;
		if (goodsId != null && goodsId > 0) {
			goods = goodsService.getById(goodsId);
		}
		GoodsVO goodsVO = goodsService.getGoodsVO(goods, request);
		cartVO.setGoodsVO(goodsVO);
		return cartVO;
	}
	
	@Override
	public Page<CartVO> getCartVOPage(Page<Cart> cartPage, HttpServletRequest request) {
		List<Cart> cartList = cartPage.getRecords();
		Page<CartVO> cartVOPage = new Page<>(cartPage.getCurrent(), cartPage.getSize(), cartPage.getTotal());
		if (CollUtil.isEmpty(cartList)) {
			return cartVOPage;
		}
		// 1. 关联查询信息
		Set<Long> goodsIdSet = cartList.stream().map(Cart::getGoodsId).collect(Collectors.toSet());
		Set<Long> userIdSet = cartList.stream().map(Cart::getUserId).collect(Collectors.toSet());
		Map<Long, List<Goods>> goodsIdGoodsListMap = goodsService.listByIds(goodsIdSet).stream()
				.collect(Collectors.groupingBy(Goods::getId));
		Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
				.collect(Collectors.groupingBy(User::getId));
		// 填充信息
		List<CartVO> cartVOList = cartList.stream().map(cart -> {
			CartVO cartVO = CartVO.objToVo(cart);
			Long goodsId = cart.getGoodsId();
			Long userId = cart.getUserId();
			Goods goods = null;
			User user = null;
			if (goodsIdGoodsListMap.containsKey(goodsId)) {
				goods = goodsIdGoodsListMap.get(goodsId).get(0);
			}
			if (userIdUserListMap.containsKey(userId)) {
				user = userIdUserListMap.get(userId).get(0);
			}
			cartVO.setGoodsVO(goodsService.getGoodsVO(goods, request));
			cartVO.setUserVO(userService.getUserVO(user));
			return cartVO;
		}).collect(Collectors.toList());
		cartVOPage.setRecords(cartVOList);
		return cartVOPage;
	}
}