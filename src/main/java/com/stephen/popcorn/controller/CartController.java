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
import com.stephen.popcorn.model.dto.cart.CartAddRequest;
import com.stephen.popcorn.model.dto.cart.CartEditRequest;
import com.stephen.popcorn.model.dto.cart.CartQueryRequest;
import com.stephen.popcorn.model.dto.cart.CartUpdateRequest;
import com.stephen.popcorn.model.entity.Cart;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.OrderItem;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.CartVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.CartService;
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
@RequestMapping("/cart")
@Slf4j
public class CartController {
	
	@Resource
	private CartService cartService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private GoodsService goodsService;
	
	/**
	 * 创建
	 *
	 * @param cartAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addCart(@RequestBody CartAddRequest cartAddRequest, HttpServletRequest request) {
		if (cartAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartAddRequest, cart);
		// 设置购买用户为当前登录用户
		User loginUser = userService.getLoginUser(request);
		cart.setUserId(loginUser.getId());
		// 检查商品是否存在
		Goods goods = goodsService.getById(cart.getGoodsId());
		if (goods == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 设置订单和商品
		cart.setGoodsId(goods.getId());
		cartService.validCart(cart, true);
		boolean result = cartService.save(cart);
		ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
		long newCartId = cart.getId();
		return ResultUtils.success(newCartId);
	}
	
	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteCart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = deleteRequest.getId();
		// 判断是否存在
		Cart oldCart = cartService.getById(id);
		ThrowUtils.throwIf(oldCart == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅管理员可删除
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = cartService.removeById(id);
		return ResultUtils.success(b);
	}
	
	/**
	 * 更新（仅管理员）
	 *
	 * @param cartUpdateRequest
	 * @return
	 */
	@PostMapping("/update")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Boolean> updateCart(@RequestBody CartUpdateRequest cartUpdateRequest) {
		if (cartUpdateRequest == null || cartUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartUpdateRequest, cart);
		
		// 参数校验
		cartService.validCart(cart, false);
		long id = cartUpdateRequest.getId();
		// 判断是否存在
		Cart oldCart = cartService.getById(id);
		ThrowUtils.throwIf(oldCart == null, ErrorCode.NOT_FOUND_ERROR);
		boolean result = cartService.updateById(cart);
		return ResultUtils.success(result);
	}
	
	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/vo")
	public BaseResponse<CartVO> getCartVOById(long id, HttpServletRequest request) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Cart cart = cartService.getById(id);
		if (cart == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		return ResultUtils.success(cartService.getCartVO(cart, request));
	}
	
	/**
	 * 分页获取列表（仅管理员）
	 *
	 * @param cartQueryRequest
	 * @return
	 */
	@PostMapping("/list/page")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Page<Cart>> listCartByPage(@RequestBody CartQueryRequest cartQueryRequest) {
		long current = cartQueryRequest.getCurrent();
		long size = cartQueryRequest.getPageSize();
		Page<Cart> cartPage = cartService.page(new Page<>(current, size),
				cartService.getQueryWrapper(cartQueryRequest));
		return ResultUtils.success(cartPage);
	}
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param cartQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<CartVO>> listCartVOByPage(@RequestBody CartQueryRequest cartQueryRequest,
	                                                        HttpServletRequest request) {
		long current = cartQueryRequest.getCurrent();
		long size = cartQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Cart> cartPage = cartService.page(new Page<>(current, size),
				cartService.getQueryWrapper(cartQueryRequest));
		return ResultUtils.success(cartService.getCartVOPage(cartPage, request));
	}
	
	/**
	 * 分页获取当前用户创建的资源列表
	 *
	 * @param cartQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/my/list/page/vo")
	public BaseResponse<Page<CartVO>> listMyCartVOByPage(@RequestBody CartQueryRequest cartQueryRequest,
	                                                          HttpServletRequest request) {
		if (cartQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long current = cartQueryRequest.getCurrent();
		long size = cartQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Cart> cartPage = cartService.page(new Page<>(current, size),
				cartService.getQueryWrapper(cartQueryRequest));
		return ResultUtils.success(cartService.getCartVOPage(cartPage, request));
	}
	
	
	/**
	 * 编辑（用户）
	 *
	 * @param cartEditRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/edit")
	public BaseResponse<Boolean> editCart(@RequestBody CartEditRequest cartEditRequest, HttpServletRequest request) {
		if (cartEditRequest == null || cartEditRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartEditRequest, cart);
		// 参数校验
		cartService.validCart(cart, false);
		User loginUser = userService.getLoginUser(request);
		long id = cartEditRequest.getId();
		// 判断是否存在
		Cart oldCart = cartService.getById(id);
		ThrowUtils.throwIf(oldCart == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅管理员可编辑
		if (!userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = cartService.updateById(cart);
		return ResultUtils.success(result);
	}
	
}