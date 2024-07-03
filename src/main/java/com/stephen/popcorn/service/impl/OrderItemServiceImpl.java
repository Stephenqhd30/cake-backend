package com.stephen.popcorn.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.constants.CommonConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.mapper.OrderItemMapper;
import com.stephen.popcorn.model.dto.orderItem.OrderItemQueryRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.entity.OrderItem;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.OrderItemVO;
import com.stephen.popcorn.model.vo.OrderVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.OrderItemService;
import com.stephen.popcorn.service.OrderService;
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
* @description 针对表【order_item(订单项表)】的数据库操作Service实现
* @createDate 2024-06-24 17:35:24
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{
	
	@Resource
	private GoodsService goodsService;
	
	@Resource
	private OrderService orderService;
	
	@Override
	public void validOrderItem(OrderItem orderItem, boolean add) {
		if (orderItem == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Double goodsPrice = orderItem.getGoodsPrice();
		Integer goodsAmount = orderItem.getGoodsAmount();
		Long goodsId = orderItem.getGoodsId();
		Long orderId = orderItem.getOrderId();
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(ObjectUtils.isEmpty(goodsId), ErrorCode.PARAMS_ERROR);
			ThrowUtils.throwIf(ObjectUtils.isEmpty(orderId), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (ObjectUtils.isEmpty(goodsPrice) || goodsPrice < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品价格不能为空或者小于0");
		}
		if (ObjectUtils.isEmpty(goodsAmount) || goodsAmount < 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品数量不能为空或者小于0");
		}
	}
	
	/**
	 * 获取查询包装类
	 *
	 * @param orderItemQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<OrderItem> getQueryWrapper(OrderItemQueryRequest orderItemQueryRequest) {
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		if (orderItemQueryRequest == null) {
			return queryWrapper;
		}
		Long id = orderItemQueryRequest.getId();
		Double goodsPrice = orderItemQueryRequest.getGoodsPrice();
		Integer goodsAmount = orderItemQueryRequest.getGoodsAmount();
		Long goodsId = orderItemQueryRequest.getGoodsId();
		Long orderId = orderItemQueryRequest.getOrderId();
		String sortField = orderItemQueryRequest.getSortField();
		String sortOrder = orderItemQueryRequest.getSortOrder();
		
		queryWrapper.eq(ObjectUtils.isNotEmpty(goodsPrice), "goodsPrice", goodsPrice);
		queryWrapper.eq(ObjectUtils.isNotEmpty(goodsAmount), "goodsAmount", goodsAmount);
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(goodsId), "goodsId", goodsId);
		queryWrapper.eq(ObjectUtils.isNotEmpty(orderId), "orderId", orderId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
	
	@Override
	public OrderItemVO getOrderItemVO(OrderItem orderItem, HttpServletRequest request) {
		OrderItemVO orderItemVO = OrderItemVO.objToVo(orderItem);
		// 1. 关联查询信息
		Long goodsId = orderItem.getGoodsId();
		Long orderId = orderItem.getOrderId();
		Goods goods = null;
		Order order = null;
		if (goodsId != null && goodsId > 0) {
			goods = goodsService.getById(goodsId);
		}
		if (orderId != null && orderId > 0) {
			order = orderService.getById(orderId);
		}
		OrderVO orderVO = orderService.getOrderVO(order, request);
		GoodsVO goodsVO = goodsService.getGoodsVO(goods, request);
		orderItemVO.setOrderVO(orderVO);
		orderItemVO.setGoodsVO(goodsVO);
		return orderItemVO;
	}
	
	@Override
	public Page<OrderItemVO> getOrderItemVOPage(Page<OrderItem> orderItemPage, HttpServletRequest request) {
		List<OrderItem> orderItemList = orderItemPage.getRecords();
		Page<OrderItemVO> orderItemVOPage = new Page<>(orderItemPage.getCurrent(), orderItemPage.getSize(), orderItemPage.getTotal());
		if (CollUtil.isEmpty(orderItemList)) {
			return orderItemVOPage;
		}
		// 1. 关联查询用户信息
		Set<Long> orderIdSet = orderItemList.stream().map(OrderItem::getOrderId).collect(Collectors.toSet());
		Set<Long> goodsIdSet = orderItemList.stream().map(OrderItem::getGoodsId).collect(Collectors.toSet());
		Map<Long, List<Goods>> goodsIdGoodsListMap = goodsService.listByIds(goodsIdSet).stream()
				.collect(Collectors.groupingBy(Goods::getId));
		Map<Long, List<Order>> orderIdOrderListMap = orderService.listByIds(orderIdSet).stream()
				.collect(Collectors.groupingBy(Order::getId));
		// 填充信息
		List<OrderItemVO> orderItemVOList = orderItemList.stream().map(orderItem -> {
			OrderItemVO orderItemVO = OrderItemVO.objToVo(orderItem);
			Long goodsId = orderItem.getGoodsId();
			Long orderId = orderItem.getOrderId();
			Goods goods = null;
			Order order = null;
			if (goodsIdGoodsListMap.containsKey(goodsId)) {
				goods = goodsIdGoodsListMap.get(goodsId).get(0);
			}
			if (orderIdOrderListMap.containsKey(orderId)) {
				order = orderIdOrderListMap.get(orderId).get(0);
			}
			orderItemVO.setGoodsVO(goodsService.getGoodsVO(goods, request));
			orderItemVO.setOrderVO(orderService.getOrderVO(order, request));
			return orderItemVO;
		}).collect(Collectors.toList());
		orderItemVOPage.setRecords(orderItemVOList);
		return orderItemVOPage;
	}
}




