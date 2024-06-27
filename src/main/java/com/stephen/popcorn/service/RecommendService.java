package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.popcorn.model.dto.recommend.RecommendQueryRequest;
import com.stephen.popcorn.model.entity.Recommend;
import com.stephen.popcorn.model.vo.RecommendVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author stephen qiu
 * @description 针对表【recommend(推荐表)】的数据库操作Service
 * @createDate 2024-06-24 17:35:21
 */
public interface RecommendService extends IService<Recommend> {
	void validRecommend(Recommend recommend, boolean add);
	
	QueryWrapper<Recommend> getQueryWrapper(RecommendQueryRequest recommendQueryRequest);
	
	RecommendVO getRecommendVO(Recommend recommend, HttpServletRequest request);
	
	Page<RecommendVO> getRecommendVOPage(Page<Recommend> postPage, HttpServletRequest request);
}
