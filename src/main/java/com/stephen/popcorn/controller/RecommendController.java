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
import com.stephen.popcorn.model.dto.recommend.RecommendAddRequest;
import com.stephen.popcorn.model.dto.recommend.RecommendEditRequest;
import com.stephen.popcorn.model.dto.recommend.RecommendQueryRequest;
import com.stephen.popcorn.model.dto.recommend.RecommendUpdateRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.Recommend;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.RecommendVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.OrderService;
import com.stephen.popcorn.service.RecommendService;
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
@RequestMapping("/recommend")
@Slf4j
public class RecommendController {
	
	@Resource
	private RecommendService recommendService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private GoodsService goodsService;
	
	/**
	 * 创建
	 *
	 * @param recommendAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addRecommend(@RequestBody RecommendAddRequest recommendAddRequest, HttpServletRequest request) {
		if (recommendAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Recommend recommend = new Recommend();
		BeanUtils.copyProperties(recommendAddRequest, recommend);
		recommendService.validRecommend(recommend, true);
		
		// 检查商品是否存在
		Goods goods = goodsService.getById(recommend.getGoodsId());
		if (goods == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 设置订单和商品
		recommend.setGoodsId(goods.getId());
		// 保存订单项
		boolean result = recommendService.save(recommend);
		ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
		// 返回新创建的订单项 ID
		long newRecommendId = recommend.getId();
		return ResultUtils.success(newRecommendId);
	}
	
	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteRecommend(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long id = deleteRequest.getId();
		// 判断是否存在
		Recommend oldRecommend = recommendService.getById(id);
		ThrowUtils.throwIf(oldRecommend == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅管理员可删除
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = recommendService.removeById(id);
		return ResultUtils.success(b);
	}
	
	/**
	 * 更新（仅管理员）
	 *
	 * @param recommendUpdateRequest
	 * @return
	 */
	@PostMapping("/update")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Boolean> updateRecommend(@RequestBody RecommendUpdateRequest recommendUpdateRequest) {
		if (recommendUpdateRequest == null || recommendUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Recommend recommend = new Recommend();
		BeanUtils.copyProperties(recommendUpdateRequest, recommend);
		
		// 参数校验
		recommendService.validRecommend(recommend, false);
		long id = recommendUpdateRequest.getId();
		// 判断是否存在
		Recommend oldRecommend = recommendService.getById(id);
		ThrowUtils.throwIf(oldRecommend == null, ErrorCode.NOT_FOUND_ERROR);
		boolean result = recommendService.updateById(recommend);
		return ResultUtils.success(result);
	}
	
	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/vo")
	public BaseResponse<RecommendVO> getRecommendVOById(long id, HttpServletRequest request) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Recommend recommend = recommendService.getById(id);
		if (recommend == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		return ResultUtils.success(recommendService.getRecommendVO(recommend, request));
	}
	
	/**
	 * 分页获取列表（仅管理员）
	 *
	 * @param recommendQueryRequest
	 * @return
	 */
	@PostMapping("/list/page")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Page<Recommend>> listRecommendByPage(@RequestBody RecommendQueryRequest recommendQueryRequest) {
		long current = recommendQueryRequest.getCurrent();
		long size = recommendQueryRequest.getPageSize();
		Page<Recommend> recommendPage = recommendService.page(new Page<>(current, size),
				recommendService.getQueryWrapper(recommendQueryRequest));
		return ResultUtils.success(recommendPage);
	}
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param recommendQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<RecommendVO>> listRecommendVOByPage(@RequestBody RecommendQueryRequest recommendQueryRequest,
	                                                             HttpServletRequest request) {
		long current = recommendQueryRequest.getCurrent();
		long size = recommendQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Recommend> recommendPage = recommendService.page(new Page<>(current, size),
				recommendService.getQueryWrapper(recommendQueryRequest));
		return ResultUtils.success(recommendService.getRecommendVOPage(recommendPage, request));
	}
	
	/**
	 * 分页获取当前用户创建的资源列表
	 *
	 * @param recommendQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/my/list/page/vo")
	public BaseResponse<Page<RecommendVO>> listMyRecommendVOByPage(@RequestBody RecommendQueryRequest recommendQueryRequest,
	                                                               HttpServletRequest request) {
		if (recommendQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long current = recommendQueryRequest.getCurrent();
		long size = recommendQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Recommend> recommendPage = recommendService.page(new Page<>(current, size),
				recommendService.getQueryWrapper(recommendQueryRequest));
		return ResultUtils.success(recommendService.getRecommendVOPage(recommendPage, request));
	}
	
	
	/**
	 * 编辑（用户）
	 *
	 * @param recommendEditRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/edit")
	public BaseResponse<Boolean> editRecommend(@RequestBody RecommendEditRequest recommendEditRequest, HttpServletRequest request) {
		if (recommendEditRequest == null || recommendEditRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Recommend recommend = new Recommend();
		BeanUtils.copyProperties(recommendEditRequest, recommend);
		// 参数校验
		recommendService.validRecommend(recommend, false);
		User loginUser = userService.getLoginUser(request);
		long id = recommendEditRequest.getId();
		// 判断是否存在
		Recommend oldRecommend = recommendService.getById(id);
		ThrowUtils.throwIf(oldRecommend == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅管理员可编辑
		if (!userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = recommendService.updateById(recommend);
		return ResultUtils.success(result);
	}
	
}
