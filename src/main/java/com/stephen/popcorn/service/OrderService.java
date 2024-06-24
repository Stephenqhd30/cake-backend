package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.popcorn.model.dto.order.OrderQueryRequest;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.entity.Order;
import com.stephen.popcorn.model.vo.OrderVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author stephen qiu
* @description 针对表【order(订单表)】的数据库操作Service
* @createDate 2024-06-24 17:35:27
*/
public interface OrderService extends IService<Order> {
	void validOrder(Order order, boolean add);
	
	QueryWrapper<Order> getQueryWrapper(OrderQueryRequest orderQueryRequest);
	
	OrderVO getOrderVO(Order order, HttpServletRequest request);
	
	Page<OrderVO> getOrderVOPage(Page<Order> orderPage, HttpServletRequest request);
}
