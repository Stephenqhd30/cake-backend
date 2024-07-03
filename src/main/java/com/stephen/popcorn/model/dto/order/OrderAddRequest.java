package com.stephen.popcorn.model.dto.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.stephen.popcorn.model.dto.orderItem.OrderItemAddRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 创建请求
 *
 * @author stephen qiu
 */
@Data
public class OrderAddRequest implements Serializable {
	
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
	 * 用户id
	 */
	private Long goodsId;
	
	private static final long serialVersionUID = 1L;
}