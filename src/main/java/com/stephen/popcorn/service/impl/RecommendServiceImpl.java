package com.stephen.popcorn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.model.entity.Recommend;
import com.stephen.popcorn.service.RecommendService;
import com.stephen.popcorn.mapper.RecommendMapper;
import org.springframework.stereotype.Service;

/**
* @author stephen qiu
* @description 针对表【recommend(推荐表)】的数据库操作Service实现
* @createDate 2024-06-24 17:35:21
*/
@Service
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend>
    implements RecommendService{

}




