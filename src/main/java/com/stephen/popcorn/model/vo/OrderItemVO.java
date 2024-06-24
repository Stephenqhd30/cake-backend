package com.stephen.popcorn.model.vo;

import com.stephen.popcorn.model.entity.OrderItem;
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
public class OrderItemVO implements Serializable {
	/**
	 * 订单id
	 */
	private Long id;
	
	/**
	 * 商品价格
	 */
	private Double orderItemPrice;
	
	/**
	 * 商品数量
	 */
	private Integer orderItemAmount;
	
	/**
	 * 商品
	 */
	private GoodsVO goodsVO;
	
	/**
	 * 订单
	 */
	private OrderVO orderVO;
	
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
	 * @param orderItemVO
	 * @return
	 */
	public static OrderItem voToObj(OrderItemVO orderItemVO) {
		if (orderItemVO == null) {
			return null;
		}
		OrderItem orderItem = new OrderItem();
		BeanUtils.copyProperties(orderItemVO, orderItem);
		return orderItem;
	}
	
	/**
	 * 对象转包装类
	 *
	 * @param orderItem
	 * @return
	 */
	public static OrderItemVO objToVo(OrderItem orderItem) {
		if (orderItem == null) {
			return null;
		}
		OrderItemVO orderItemVO = new OrderItemVO();
		BeanUtils.copyProperties(orderItem, orderItemVO);
		return orderItemVO;
	}
}
