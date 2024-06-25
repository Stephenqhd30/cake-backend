package com.stephen.popcorn.model.dto.type;

import com.stephen.popcorn.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author stephen qiu
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TypeQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    
    
    /**
     * 商品名称
     */
    private String typeName;
    
    /**
     * 关键词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}