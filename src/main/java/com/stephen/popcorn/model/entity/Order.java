package com.stephen.popcorn.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单表
 * @author stephen qiu
 * @TableName order
 */
@TableName(value ="order")
@Data
public class Order implements Serializable {
    /**
     * 订单id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 总金额
     */
    private Double total;

    /**
     * 商品数量
     */
    private Integer amount;

    /**
     * 支付状态
     */
    private Integer status;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户地址
     */
    private String address;

    /**
     * 订单日期
     */
    private Date dateTime;

    /**
     * 用户id
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