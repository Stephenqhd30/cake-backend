package com.stephen.popcorn.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.annotation.AuthCheck;
import com.stephen.popcorn.common.BaseResponse;
import com.stephen.popcorn.common.DeleteRequest;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.common.ResultUtils;
import com.stephen.popcorn.constants.UserConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.order.OrderAddRequest;
import com.stephen.popcorn.model.dto.order.OrderEditRequest;
import com.stephen.popcorn.model.dto.order.OrderQueryRequest;
import com.stephen.popcorn.model.dto.order.OrderUpdateRequest;
import com.stephen.popcorn.model.dto.orderItem.OrderItemAddRequest;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.entity.OrderItem;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.OrderVO;
import com.stephen.popcorn.service.OrderItemService;
import com.stephen.popcorn.service.OrderService;
import com.stephen.popcorn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 订单接口
 *
 * @author stephen qiu
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private OrderItemService orderItemService;
	
	@Resource
	private UserService userService;
	
	/**
	 * 创建
	 *
	 * @param orderAddRequest 订单的添加请求
	 * @param request httpServletRequest
	 * @return 添加成功之后的订单 id
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addOrder(@RequestBody OrderAddRequest orderAddRequest, HttpServletRequest request) {
		if (orderAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Order order = new Order();
		BeanUtils.copyProperties(orderAddRequest, order);
		orderService.validOrder(order, true);
		// 设置添加创建人为当前用户
		User loginUser = userService.getLoginUser(request);
		order.setUserId(loginUser.getId());
		boolean result = orderService.save(order);
		ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
		long newOrderId = order.getId();
		// 创建订单项
		OrderItem orderItem = new OrderItem();
		orderItem.setGoodsPrice(orderAddRequest.getTotal());
		orderItem.setGoodsAmount(orderAddRequest.getAmount());
		orderItem.setGoodsId(orderAddRequest.getGoodsId());
		orderItem.setOrderId(order.getId());
		boolean orderItemResult = orderItemService.save(orderItem);
		ThrowUtils.throwIf(!orderItemResult, ErrorCode.OPERATION_ERROR);
		return ResultUtils.success(newOrderId);
	}
	
	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		Order oldOrder = orderService.getById(id);
		ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅本人或管理员可删除
		if (!oldOrder.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = orderService.removeById(id);
		return ResultUtils.success(b);
	}
	
	/**
	 * 更新（仅管理员）
	 *
	 * @param orderUpdateRequest 订单更新请求
	 * @return 是否更新成功
	 */
	@PostMapping("/update")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Boolean> updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest) {
		if (orderUpdateRequest == null || orderUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Order order = new Order();
		BeanUtils.copyProperties(orderUpdateRequest, order);
		
		// 参数校验
		orderService.validOrder(order, false);
		long id = orderUpdateRequest.getId();
		// 判断是否存在
		Order oldOrder = orderService.getById(id);
		ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);
		boolean result = orderService.updateById(order);
		return ResultUtils.success(result);
	}
	
	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/vo")
	public BaseResponse<OrderVO> getOrderVOById(long id, HttpServletRequest request) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Order order = orderService.getById(id);
		if (order == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		return ResultUtils.success(orderService.getOrderVO(order, request));
	}
	
	/**
	 * 分页获取列表（仅管理员）
	 *
	 * @param orderQueryRequest
	 * @return
	 */
	@PostMapping("/list/page")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Page<Order>> listOrderByPage(@RequestBody OrderQueryRequest orderQueryRequest) {
		long current = orderQueryRequest.getCurrent();
		long size = orderQueryRequest.getPageSize();
		Page<Order> orderPage = orderService.page(new Page<>(current, size),
				orderService.getQueryWrapper(orderQueryRequest));
		return ResultUtils.success(orderPage);
	}
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param orderQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<OrderVO>> listOrderVOByPage(@RequestBody OrderQueryRequest orderQueryRequest,
	                                                   HttpServletRequest request) {
		long current = orderQueryRequest.getCurrent();
		long size = orderQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Order> orderPage = orderService.page(new Page<>(current, size),
				orderService.getQueryWrapper(orderQueryRequest));
		return ResultUtils.success(orderService.getOrderVOPage(orderPage, request));
	}
	
	/**
	 * 分页获取当前用户创建的资源列表
	 *
	 * @param orderQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/my/list/page/vo")
	public BaseResponse<Page<OrderVO>> listMyOrderVOByPage(@RequestBody OrderQueryRequest orderQueryRequest,
	                                                     HttpServletRequest request) {
		if (orderQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		orderQueryRequest.setUserId(loginUser.getId());
		long current = orderQueryRequest.getCurrent();
		long size = orderQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Order> orderPage = orderService.page(new Page<>(current, size),
				orderService.getQueryWrapper(orderQueryRequest));
		return ResultUtils.success(orderService.getOrderVOPage(orderPage, request));
	}
	
	// endregion
	
	
	/**
	 * 编辑（用户）
	 *
	 * @param orderEditRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/edit")
	public BaseResponse<Boolean> editOrder(@RequestBody OrderEditRequest orderEditRequest, HttpServletRequest request) {
		if (orderEditRequest == null || orderEditRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Order order = new Order();
		BeanUtils.copyProperties(orderEditRequest, order);
		// 参数校验
		orderService.validOrder(order, false);
		User loginUser = userService.getLoginUser(request);
		long id = orderEditRequest.getId();
		// 判断是否存在
		Order oldOrder = orderService.getById(id);
		ThrowUtils.throwIf(oldOrder == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅本人或管理员可编辑
		if (!oldOrder.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = orderService.updateById(order);
		return ResultUtils.success(result);
	}
}
