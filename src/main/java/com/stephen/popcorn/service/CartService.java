package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.popcorn.model.dto.cart.CartQueryRequest;
import com.stephen.popcorn.model.entity.Cart;
import com.stephen.popcorn.model.vo.CartVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author stephen qiu
 * @description 针对表【cart(推荐表)】的数据库操作Service
 * @createDate 2024-06-24 17:35:21
 */
public interface CartService extends IService<Cart> {
	void validCart(Cart cart, boolean add);
	
	QueryWrapper<Cart> getQueryWrapper(CartQueryRequest cartQueryRequest);
	
	CartVO getCartVO(Cart cart, HttpServletRequest request);
	
	Page<CartVO> getCartVOPage(Page<Cart> postPage, HttpServletRequest request);
}
