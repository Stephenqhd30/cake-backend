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
import com.stephen.popcorn.model.dto.orderItem.OrderItemAddRequest;
import com.stephen.popcorn.model.dto.orderItem.OrderItemEditRequest;
import com.stephen.popcorn.model.dto.orderItem.OrderItemQueryRequest;
import com.stephen.popcorn.model.dto.orderItem.OrderItemUpdateRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.entity.OrderItem;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.OrderItemVO;
import com.stephen.popcorn.service.GoodsService;
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
@RequestMapping("/orderItem")
@Slf4j
public class OrderItemController {
	
	@Resource
	private OrderItemService orderItemService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private OrderService orderService;
	
	@Resource
	private GoodsService goodsService;
	
	/**
	 * 创建
	 *
	 * @param orderItemAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addOrderItem(@RequestBody OrderItemAddRequest orderItemAddRequest, HttpServletRequest request) {
		if (orderItemAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		OrderItem orderItem = new OrderItem();
		BeanUtils.copyProperties(orderItemAddRequest, orderItem);
		orderItemService.validOrderItem(orderItem, true);
		
		// 检查订单是否存在
		Order order = orderService.getById(orderItem.getOrderId());
		if (order == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 检查商品是否存在
		Goods goods = goodsService.getById(orderItem.getGoodsId());
		if (goods == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 设置订单和商品
		orderItem.setOrderId(order.getId());
		orderItem.setGoodsId(goods.getId());
		// 保存订单项
		boolean result = orderItemService.save(orderItem);
		ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
		// 返回新创建的订单项 ID
		long newOrderItemId = orderItem.getId();
		return ResultUtils.success(newOrderItemId);
	}
	
	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteOrderItem(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		OrderItem oldOrderItem = orderItemService.getById(id);
		ThrowUtils.throwIf(oldOrderItem == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅管理员可删除
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = orderItemService.removeById(id);
		return ResultUtils.success(b);
	}
	
	/**
	 * 更新（仅管理员）
	 *
	 * @param orderItemUpdateRequest
	 * @return
	 */
	@PostMapping("/update")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Boolean> updateOrderItem(@RequestBody OrderItemUpdateRequest orderItemUpdateRequest) {
		if (orderItemUpdateRequest == null || orderItemUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		OrderItem orderItem = new OrderItem();
		BeanUtils.copyProperties(orderItemUpdateRequest, orderItem);
		
		// 参数校验
		orderItemService.validOrderItem(orderItem, false);
		long id = orderItemUpdateRequest.getId();
		// 判断是否存在
		OrderItem oldOrderItem = orderItemService.getById(id);
		ThrowUtils.throwIf(oldOrderItem == null, ErrorCode.NOT_FOUND_ERROR);
		boolean result = orderItemService.updateById(orderItem);
		return ResultUtils.success(result);
	}
	
	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/vo")
	public BaseResponse<OrderItemVO> getOrderItemVOById(long id, HttpServletRequest request) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		OrderItem orderItem = orderItemService.getById(id);
		if (orderItem == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		return ResultUtils.success(orderItemService.getOrderItemVO(orderItem, request));
	}
	
	/**
	 * 分页获取列表（仅管理员）
	 *
	 * @param orderItemQueryRequest
	 * @return
	 */
	@PostMapping("/list/page")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Page<OrderItem>> listOrderItemByPage(@RequestBody OrderItemQueryRequest orderItemQueryRequest) {
		long current = orderItemQueryRequest.getCurrent();
		long size = orderItemQueryRequest.getPageSize();
		Page<OrderItem> orderItemPage = orderItemService.page(new Page<>(current, size),
				orderItemService.getQueryWrapper(orderItemQueryRequest));
		return ResultUtils.success(orderItemPage);
	}
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param orderItemQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<OrderItemVO>> listOrderItemVOByPage(@RequestBody OrderItemQueryRequest orderItemQueryRequest,
	                                                   HttpServletRequest request) {
		long current = orderItemQueryRequest.getCurrent();
		long size = orderItemQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<OrderItem> orderItemPage = orderItemService.page(new Page<>(current, size),
				orderItemService.getQueryWrapper(orderItemQueryRequest));
		return ResultUtils.success(orderItemService.getOrderItemVOPage(orderItemPage, request));
	}
	
	/**
	 * 分页获取当前用户创建的资源列表
	 *
	 * @param orderItemQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/my/list/page/vo")
	public BaseResponse<Page<OrderItemVO>> listMyOrderItemVOByPage(@RequestBody OrderItemQueryRequest orderItemQueryRequest,
	                                                     HttpServletRequest request) {
		if (orderItemQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		orderItemQueryRequest.setUserId(loginUser.getId());
		long current = orderItemQueryRequest.getCurrent();
		long size = orderItemQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<OrderItem> orderItemPage = orderItemService.page(new Page<>(current, size),
				orderItemService.getQueryWrapper(orderItemQueryRequest));
		return ResultUtils.success(orderItemService.getOrderItemVOPage(orderItemPage, request));
	}
	
	
	
	/**
	 * 编辑（用户）
	 *
	 * @param orderItemEditRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/edit")
	public BaseResponse<Boolean> editOrderItem(@RequestBody OrderItemEditRequest orderItemEditRequest, HttpServletRequest request) {
		if (orderItemEditRequest == null || orderItemEditRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		OrderItem orderItem = new OrderItem();
		BeanUtils.copyProperties(orderItemEditRequest, orderItem);
		// 参数校验
		orderItemService.validOrderItem(orderItem, false);
		User loginUser = userService.getLoginUser(request);
		long id = orderItemEditRequest.getId();
		// 判断是否存在
		OrderItem oldOrderItem = orderItemService.getById(id);
		ThrowUtils.throwIf(oldOrderItem == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅管理员可编辑
		if (!userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = orderItemService.updateById(orderItem);
		return ResultUtils.success(result);
	}
	
}
