package com.stephen.popcorn.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 推荐表
 * @TableName recommend
 */
@TableName(value ="recommend")
@Data
public class Recommend implements Serializable {
    /**
     * 推荐栏id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品类别
     */
    private String goodsType;

    /**
     * 商品id
     */
    private Long goodsId;

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