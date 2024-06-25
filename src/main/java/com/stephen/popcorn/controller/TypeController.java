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
import com.stephen.popcorn.model.dto.type.TypeAddRequest;
import com.stephen.popcorn.model.dto.type.TypeEditRequest;
import com.stephen.popcorn.model.dto.type.TypeQueryRequest;
import com.stephen.popcorn.model.dto.type.TypeUpdateRequest;
import com.stephen.popcorn.model.entity.Type;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.vo.TypeVO;
import com.stephen.popcorn.service.TypeService;
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
@RequestMapping("/type")
@Slf4j
public class TypeController {
	
	@Resource
	private TypeService typeService;
	
	@Resource
	private UserService userService;
	
	/**
	 * 创建
	 *
	 * @param typeAddRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/add")
	public BaseResponse<Long> addType(@RequestBody TypeAddRequest typeAddRequest, HttpServletRequest request) {
		if (typeAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Type type = new Type();
		BeanUtils.copyProperties(typeAddRequest, type);
		typeService.validType(type, true);
		boolean result = typeService.save(type);
		ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
		long newTypeId = type.getId();
		return ResultUtils.success(newTypeId);
	}
	
	/**
	 * 删除
	 *
	 * @param deleteRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteType(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.getLoginUser(request);
		long id = deleteRequest.getId();
		// 判断是否存在
		Type oldType = typeService.getById(id);
		ThrowUtils.throwIf(oldType == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅本人或管理员可删除
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean b = typeService.removeById(id);
		return ResultUtils.success(b);
	}
	
	/**
	 * 更新（仅管理员）
	 *
	 * @param typeUpdateRequest
	 * @return
	 */
	@PostMapping("/update")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Boolean> updateType(@RequestBody TypeUpdateRequest typeUpdateRequest) {
		if (typeUpdateRequest == null || typeUpdateRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Type type = new Type();
		BeanUtils.copyProperties(typeUpdateRequest, type);
		
		// 参数校验
		typeService.validType(type, false);
		long id = typeUpdateRequest.getId();
		// 判断是否存在
		Type oldType = typeService.getById(id);
		ThrowUtils.throwIf(oldType == null, ErrorCode.NOT_FOUND_ERROR);
		boolean result = typeService.updateById(type);
		return ResultUtils.success(result);
	}
	
	/**
	 * 根据 id 获取
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/get/vo")
	public BaseResponse<TypeVO> getTypeVOById(long id, HttpServletRequest request) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Type type = typeService.getById(id);
		if (type == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		return ResultUtils.success(typeService.getTypeVO(type, request));
	}
	
	/**
	 * 分页获取列表（仅管理员）
	 *
	 * @param typeQueryRequest
	 * @return
	 */
	@PostMapping("/list/page")
	@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
	public BaseResponse<Page<Type>> listTypeByPage(@RequestBody TypeQueryRequest typeQueryRequest) {
		long current = typeQueryRequest.getCurrent();
		long size = typeQueryRequest.getPageSize();
		Page<Type> typePage = typeService.page(new Page<>(current, size),
				typeService.getQueryWrapper(typeQueryRequest));
		return ResultUtils.success(typePage);
	}
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param typeQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<TypeVO>> listTypeVOByPage(@RequestBody TypeQueryRequest typeQueryRequest,
	                                                   HttpServletRequest request) {
		long current = typeQueryRequest.getCurrent();
		long size = typeQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Type> typePage = typeService.page(new Page<>(current, size),
				typeService.getQueryWrapper(typeQueryRequest));
		return ResultUtils.success(typeService.getTypeVOPage(typePage, request));
	}
	
	/**
	 * 分页获取当前用户创建的资源列表
	 *
	 * @param typeQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/my/list/page/vo")
	public BaseResponse<Page<TypeVO>> listMyTypeVOByPage(@RequestBody TypeQueryRequest typeQueryRequest,
	                                                     HttpServletRequest request) {
		if (typeQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		long current = typeQueryRequest.getCurrent();
		long size = typeQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Type> typePage = typeService.page(new Page<>(current, size),
				typeService.getQueryWrapper(typeQueryRequest));
		return ResultUtils.success(typeService.getTypeVOPage(typePage, request));
	}
	
	// endregion
	
	
	/**
	 * 编辑（用户）
	 *
	 * @param typeEditRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/edit")
	public BaseResponse<Boolean> editType(@RequestBody TypeEditRequest typeEditRequest, HttpServletRequest request) {
		if (typeEditRequest == null || typeEditRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Type type = new Type();
		BeanUtils.copyProperties(typeEditRequest, type);
		// 参数校验
		typeService.validType(type, false);
		User loginUser = userService.getLoginUser(request);
		long id = typeEditRequest.getId();
		// 判断是否存在
		Type oldType = typeService.getById(id);
		ThrowUtils.throwIf(oldType == null, ErrorCode.NOT_FOUND_ERROR);
		// 仅本人或管理员可编辑
		if (!userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		boolean result = typeService.updateById(type);
		return ResultUtils.success(result);
	}
	
}
