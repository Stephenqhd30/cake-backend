package com.stephen.popcorn.model.dto.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新请求
 *
 * @author stephen qiu
 */
@Data
public class OrderUpdateRequest implements Serializable {
	
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
	
	private static final long serialVersionUID = 1L;
}