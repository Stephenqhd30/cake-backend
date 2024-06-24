package com.stephen.popcorn.model.dto.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
public class OrderQueryRequest extends PageRequest implements Serializable {
    
    /**
     * 订单id
     */
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
     * 关键词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}