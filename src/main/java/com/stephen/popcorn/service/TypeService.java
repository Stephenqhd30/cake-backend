package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.popcorn.model.dto.type.TypeQueryRequest;
import com.stephen.popcorn.model.entity.Type;
import com.stephen.popcorn.model.vo.TypeVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author stephen qiu
* @description 针对表【type(类别表)】的数据库操作Service
* @createDate 2024-06-24 17:35:17
*/
public interface TypeService extends IService<Type> {
	
	void validType(Type type, boolean add);
	
	QueryWrapper<Type> getQueryWrapper(TypeQueryRequest typeQueryRequest);
	
	TypeVO getTypeVO(Type type, HttpServletRequest request);
	
	Page<TypeVO> getTypeVOPage(Page<Type> typePage, HttpServletRequest request);
}
