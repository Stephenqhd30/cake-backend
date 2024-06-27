package com.stephen.popcorn.model.dto.orderItem;

import com.stephen.popcorn.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author stephen qiu
 * 
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderItemQueryRequest extends PageRequest implements Serializable {
    
    /**
     * 订单id
     */
    private Long id;
    
    /**
     * 商品价格
     */
    private Double goodsPrice;
    
    /**
     * 商品数量
     */
    private Integer goodsAmount;
    
    /**
     * 商品id
     */
    private Long goodsId;
    
    /**
     * 订单id
     */
    private Long orderId;
    
    /**
     * 用户
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}