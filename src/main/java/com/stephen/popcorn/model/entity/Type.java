package com.stephen.popcorn.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 类别表
 * @TableName type
 */
@TableName(value ="type")
@Data
public class Type implements Serializable {
    /**
     * 商品id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品名称
     */
    private String typeName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}