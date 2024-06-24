package com.stephen.popcorn.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品表
 * @author stephen qiu
 * @TableName goods
 */
@TableName(value ="goods")
@Data
public class Goods implements Serializable {
    /**
     * 商品id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品封面图
     */
    private String goodsCover;

    /**
     * 商品详细图1
     */
    private String goodsImage1;

    /**
     * 商品详细图2
     */
    private String goodsImage2;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 商品介绍
     */
    private String content;

    /**
     * 商品库存
     */
    private Integer stock;
    
    /**
     * 商品类型
     */
    private Integer typeId;

    /**
     * 创建人id
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