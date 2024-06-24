package com.stephen.popcorn.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.popcorn.model.entity.Type;
import com.stephen.popcorn.service.TypeService;
import com.stephen.popcorn.mapper.TypeMapper;
import org.springframework.stereotype.Service;

/**
* @author stephen qiu
* @description 针对表【type(类别表)】的数据库操作Service实现
* @createDate 2024-06-24 17:35:17
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{

}




