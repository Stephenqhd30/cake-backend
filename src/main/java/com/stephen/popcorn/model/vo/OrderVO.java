package com.stephen.popcorn.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.stephen.popcorn.model.entity.Order;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 帖子视图
 *
 * @author stephen qiu
 */
@Data
public class OrderVO implements Serializable {
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
	
	/**
	 * 用户
	 */
	private UserVO userVO;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 包装类转对象
	 *
	 * @param orderVO
	 * @return
	 */
	public static Order voToObj(OrderVO orderVO) {
		if (orderVO == null) {
			return null;
		}
		Order goods = new Order();
		BeanUtils.copyProperties(orderVO, goods);
		return goods;
	}
	
	/**
	 * 对象转包装类
	 *
	 * @param goods
	 * @return
	 */
	public static OrderVO objToVo(Order goods) {
		if (goods == null) {
			return null;
		}
		OrderVO orderVO = new OrderVO();
		BeanUtils.copyProperties(goods, orderVO);
		return orderVO;
	}
}
