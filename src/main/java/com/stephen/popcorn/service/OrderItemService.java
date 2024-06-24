package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.popcorn.model.dto.orderItem.OrderItemQueryRequest;
import com.stephen.popcorn.model.entity.OrderItem;
import com.stephen.popcorn.model.vo.OrderItemVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author stephen qiu
* @description 针对表【order_item(订单项表)】的数据库操作Service
* @createDate 2024-06-24 17:35:24
*/
public interface OrderItemService extends IService<OrderItem> {
	
	void validOrderItem(OrderItem orderItem, boolean add);
	
	QueryWrapper<OrderItem> getQueryWrapper(OrderItemQueryRequest orderItemQueryRequest);
	
	OrderItemVO getOrderItemVO(OrderItem orderItem, HttpServletRequest request);
	
	Page<OrderItemVO> getOrderItemVOPage(Page<OrderItem> postPage, HttpServletRequest request);
}
