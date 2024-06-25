package com.stephen.popcorn.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.constants.CommonConstant;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.type.TypeQueryRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.Type;
import com.stephen.popcorn.model.entity.Type;
import com.stephen.popcorn.model.entity.User;
import com.stephen.popcorn.model.enums.GoodsTypeEnum;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.TypeVO;
import com.stephen.popcorn.model.vo.UserVO;
import com.stephen.popcorn.service.TypeService;
import com.stephen.popcorn.mapper.TypeMapper;
import com.stephen.popcorn.service.UserService;
import com.stephen.popcorn.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author stephen qiu
* @description 针对表【type(类别表)】的数据库操作Service实现
* @createDate 2024-06-24 17:35:17
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{
	@Resource
	private UserService userService;
	
	@Override
	public void validType(Type type, boolean add) {
		if (type == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String typeName = type.getTypeName();
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(typeName), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (StringUtils.isNotBlank(typeName) && typeName.length() > 8192) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
		}
	}
	
	/**
	 * 获取查询包装类
	 *
	 * @param typeQueryRequest
	 * @return
	 */
	@Override
	public QueryWrapper<Type> getQueryWrapper(TypeQueryRequest typeQueryRequest) {
		QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
		if (typeQueryRequest == null) {
			return queryWrapper;
		}
		Long id = typeQueryRequest.getId();
		String typeName = typeQueryRequest.getTypeName();
		String searchText = typeQueryRequest.getSearchText();
		String sortField = typeQueryRequest.getSortField();
		String sortOrder = typeQueryRequest.getSortOrder();
		
		// 拼接查询条件
		if (StringUtils.isNotBlank(searchText)) {
			queryWrapper.and(qw -> qw.like("typeName", searchText).or().like("content", searchText));
		}
		queryWrapper.like(StringUtils.isNotBlank(typeName), "typeName", typeName);
		queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
	
	@Override
	public TypeVO getTypeVO(Type type, HttpServletRequest request) {
		return TypeVO.objToVo(type);
	}
	
	@Override
	public Page<TypeVO> getTypeVOPage(Page<Type> typePage, HttpServletRequest request) {
		List<Type> typeList = typePage.getRecords();
		Page<TypeVO> typeVOPage = new Page<>(typePage.getCurrent(), typePage.getSize(), typePage.getTotal());
		if (CollUtil.isEmpty(typeList)) {
			return typeVOPage;
		}
		// 填充信息
		List<TypeVO> typeVOList = typeList.stream().map(type -> {
			TypeVO typeVO = TypeVO.objToVo(type);
			String typeName = type.getTypeName();
			typeVO.setTypeName(GoodsTypeEnum.getEnumByValue(typeName).getText());
			return typeVO;
		}).collect(Collectors.toList());
		typeVOPage.setRecords(typeVOList);
		return typeVOPage;
		
		
	}
}




