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
import com.stephen.popcorn.model.dto.goods.GoodsAddRequest;
import com.stephen.popcorn.model.dto.goods.GoodsEditRequest;
import com.stephen.popcorn.model.dto.goods.GoodsQueryRequest;
import com.stephen.popcorn.model.dto.goods.GoodsUpdateRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 商品接口
 *
 * @author stephen qiu
 */
@RestController
@RequestMapping("/goods")
@Slf4j
public class GoodsController {
	
	@Resource
	private GoodsService goodsService;
	
	@Resource
	private UserService userService;
	
	/**
	 * 创建
	 *
	 * @param goodsAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addGoods(@RequestBody GoodsAddRequest goodsAddRequest, HttpServletRequest request) {
		if (goodsAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Goods goods = new Goods();
		BeanUtils.copyProperties(goodsAddRequest, goods);
		goodsService.validGoods(goods, true);
		User loginUser = userService.getLoginUser(request);
		goods.setUserId(loginUser.getId());
		boolean result = goodsService.save(goods);
		ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
		long newGoodsId = goods.getId();
		return ResultUtils.success(newGoodsId);
	}
	
	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteGoods(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		Goods oldGoods = goodsService.getById(id);
		ThrowUtils.throwIf(oldGoods == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅本人或管理员可删除
		if (!oldGoods.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = goodsService.removeById(id);
		return ResultUtils.success(b);
	}
	
	/**
	 * 更新（仅管理员）
	 *
	 * @param goodsUpdateRequest
	 * @return
	 */
	@PostMapping("/update")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Boolean> updateGoods(@RequestBody GoodsUpdateRequest goodsUpdateRequest) {
		if (goodsUpdateRequest == null || goodsUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Goods goods = new Goods();
		BeanUtils.copyProperties(goodsUpdateRequest, goods);
		
		// 参数校验
		goodsService.validGoods(goods, false);
		long id = goodsUpdateRequest.getId();
		// 判断是否存在
		Goods oldGoods = goodsService.getById(id);
		ThrowUtils.throwIf(oldGoods == null, ErrorCode.NOT_FOUND_ERROR);
		boolean result = goodsService.updateById(goods);
		return ResultUtils.success(result);
	}
	
	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/vo")
	public BaseResponse<GoodsVO> getGoodsVOById(long id, HttpServletRequest request) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Goods goods = goodsService.getById(id);
		if (goods == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		return ResultUtils.success(goodsService.getGoodsVO(goods, request));
	}
	
	/**
	 * 分页获取列表（仅管理员）
	 *
	 * @param goodsQueryRequest
	 * @return
	 */
	@PostMapping("/list/page")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Page<Goods>> listGoodsByPage(@RequestBody GoodsQueryRequest goodsQueryRequest) {
		long current = goodsQueryRequest.getCurrent();
		long size = goodsQueryRequest.getPageSize();
		Page<Goods> goodsPage = goodsService.page(new Page<>(current, size),
				goodsService.getQueryWrapper(goodsQueryRequest));
		return ResultUtils.success(goodsPage);
	}
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param goodsQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<GoodsVO>> listGoodsVOByPage(@RequestBody GoodsQueryRequest goodsQueryRequest,
	                                                     HttpServletRequest request) {
		long current = goodsQueryRequest.getCurrent();
		long size = goodsQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Goods> goodsPage = goodsService.page(new Page<>(current, size),
				goodsService.getQueryWrapper(goodsQueryRequest));
		return ResultUtils.success(goodsService.getGoodsVOPage(goodsPage, request));
	}
	
	/**
	 * 分页获取当前用户创建的资源列表
	 *
	 * @param goodsQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/my/list/page/vo")
	public BaseResponse<Page<GoodsVO>> listMyGoodsVOByPage(@RequestBody GoodsQueryRequest goodsQueryRequest,
	                                                       HttpServletRequest request) {
		if (goodsQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		goodsQueryRequest.setUserId(loginUser.getId());
		long current = goodsQueryRequest.getCurrent();
		long size = goodsQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Goods> goodsPage = goodsService.page(new Page<>(current, size),
				goodsService.getQueryWrapper(goodsQueryRequest));
		return ResultUtils.success(goodsService.getGoodsVOPage(goodsPage, request));
	}
	
	// endregion
	
	
	/**
	 * 编辑（用户）
	 *
	 * @param goodsEditRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/edit")
	public BaseResponse<Boolean> editGoods(@RequestBody GoodsEditRequest goodsEditRequest, HttpServletRequest request) {
		if (goodsEditRequest == null || goodsEditRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Goods goods = new Goods();
		BeanUtils.copyProperties(goodsEditRequest, goods);
		// 参数校验
		goodsService.validGoods(goods, false);
		User loginUser = userService.getLoginUser(request);
		long id = goodsEditRequest.getId();
		// 判断是否存在
		Goods oldGoods = goodsService.getById(id);
		ThrowUtils.throwIf(oldGoods == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅本人或管理员可编辑
		if (!oldGoods.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = goodsService.updateById(goods);
		return ResultUtils.success(result);
	}
	
}
