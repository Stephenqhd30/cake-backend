package com.stephen.popcorn.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.constants.CommonConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.mapper.OrderMapper;
import com.stephen.popcorn.model.dto.order.OrderQueryRequest;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.enums.OrderEnum;
import com.stephen.popcorn.model.vo.OrderVO;
import com.stephen.popcorn.model.vo.UserVO;
import com.stephen.popcorn.service.OrderService;
import com.stephen.popcorn.service.UserService;
import com.stephen.popcorn.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author stephen qiu
 * @description 针对表【order(订单表)】的数据库操作Service实现
 * @createDate 2024-06-24 17:35:27
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
		implements OrderService {
	@Resource
	private UserService userService;
	
	@Override
	public void validOrder(Order order, boolean add) {
		if (order == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Integer status = order.getStatus();
		String userName = order.getUserName();
		String userPhone = order.getUserPhone();
		String address = order.getAddress();
		Double total = order.getTotal();
		// 创建时，参数不能为空
		ThrowUtils.throwIf(OrderEnum.getEnumByValue(status).getValue() == 1, ErrorCode.PARAMS_ERROR);
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(userName, userPhone, address), ErrorCode.PARAMS_ERROR);
			ThrowUtils.throwIf(ObjectUtils.isEmpty(total), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (ObjectUtils.isEmpty(userName)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名称不能为空");
		}
		if (ObjectUtils.isEmpty(total)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "商品总价不能为空");
		}
		if (ObjectUtils.isEmpty(userPhone)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户联系电话不能为空");
		}
		if (StringUtils.isBlank(address) && address.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "地址过长");
		}
	}
	
	/**
	 * 获取查询包装类
	 *
	 * @param orderQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<Order> getQueryWrapper(OrderQueryRequest orderQueryRequest) {
		QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
		if (orderQueryRequest == null) {
			return queryWrapper;
		}
		Long id = orderQueryRequest.getId();
		Double total = orderQueryRequest.getTotal();
		Integer amount = orderQueryRequest.getAmount();
		String userName = orderQueryRequest.getUserName();
		String userPhone = orderQueryRequest.getUserPhone();
		String address = orderQueryRequest.getAddress();
		Long userId = orderQueryRequest.getUserId();
		String searchText = orderQueryRequest.getSearchText();
		String sortField = orderQueryRequest.getSortField();
		String sortOrder = orderQueryRequest.getSortOrder();
		
		
		// 拼接查询条件
		if (StringUtils.isNotBlank(searchText)) {
			queryWrapper.and(qw -> qw.like("userName", searchText).or().like("content", searchText));
		}
		queryWrapper.like(ObjectUtils.isNotEmpty(total), "total", total);
		queryWrapper.like(ObjectUtils.isNotEmpty(amount), "amount", amount);
		queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
		queryWrapper.like(StringUtils.isNotBlank(userPhone), "userPhone", userPhone);
		queryWrapper.like(StringUtils.isNotBlank(address), "address", address);
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
	
	@Override
	public OrderVO getOrderVO(Order order, HttpServletRequest request) {
		OrderVO orderVO = OrderVO.objToVo(order);
		// 1. 关联查询用户信息
		Long userId = order.getUserId();
		User user = null;
		if (userId != null && userId > 0) {
			user = userService.getById(userId);
		}
		UserVO userVO = userService.getUserVO(user);
		orderVO.setUserVO(userVO);
		return orderVO;
	}
	
	@Override
	public Page<OrderVO> getOrderVOPage(Page<Order> orderPage, HttpServletRequest request) {
		List<Order> orderList = orderPage.getRecords();
		Page<OrderVO> orderVOPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
		if (CollUtil.isEmpty(orderList)) {
			return orderVOPage;
		}
		// 1. 关联查询用户信息
		Set<Long> userIdSet = orderList.stream().map(Order::getUserId).collect(Collectors.toSet());
		Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
				.collect(Collectors.groupingBy(User::getId));
		// 填充信息
		List<OrderVO> orderVOList = orderList.stream().map(post -> {
			OrderVO orderVO = OrderVO.objToVo(post);
			Long userId = post.getUserId();
			User user = null;
			if (userIdUserListMap.containsKey(userId)) {
				user = userIdUserListMap.get(userId).get(0);
			}
			orderVO.setUserVO(userService.getUserVO(user));
			return orderVO;
		}).collect(Collectors.toList());
		orderVOPage.setRecords(orderVOList);
		return orderVOPage;
	}
}