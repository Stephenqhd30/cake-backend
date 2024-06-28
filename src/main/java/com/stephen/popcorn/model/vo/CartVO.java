package com.stephen.popcorn.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.stephen.popcorn.model.entity.Cart;
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
public class CartVO implements Serializable {
	/**
	 * 购物车id
	 */
	private Long id;
	
	/**
	 * 商品id
	 */
	private GoodsVO goodsVO;
	
	
	/**
	 * 购买数量
	 */
	private Integer quantity;
	
	
	/**
	 * 购买人id
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
	 * @param cartVO
	 * @return
	 */
	public static Cart voToObj(CartVO cartVO) {
		if (cartVO == null) {
			return null;
		}
		Cart cart = new Cart();
		BeanUtils.copyProperties(cartVO, cart);
		return cart;
	}
	
	/**
	 * 对象转包装类
	 *
	 * @param cart
	 * @return
	 */
	public static CartVO objToVo(Cart cart) {
		if (cart == null) {
			return null;
		}
		CartVO cartVO = new CartVO();
		BeanUtils.copyProperties(cart, cartVO);
		return cartVO;
	}
}
