package com.stephen.popcorn.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 购物车表
 * @author stephen qiu
 * @TableName cart
 */
@TableName(value ="cart")
@Data
public class Cart implements Serializable {
    /**
     * 购物车id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品id
     */
    private Long goodsId;
    
    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 购买人id
     */
    private Long userId;

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