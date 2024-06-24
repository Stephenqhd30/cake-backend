package com.stephen.popcorn.model.vo;

import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.entity.User;
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
public class GoodsVO implements Serializable {
	/**
	 * 商品id
	 */
	private Integer id;
	
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
	 * 创建人信信息
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
	 * @param goodsVO
	 * @return
	 */
	public static Goods voToObj(GoodsVO goodsVO) {
		if (goodsVO == null) {
			return null;
		}
		Goods goods = new Goods();
		BeanUtils.copyProperties(goodsVO, goods);
		return goods;
	}
	
	/**
	 * 对象转包装类
	 *
	 * @param goods
	 * @return
	 */
	public static GoodsVO objToVo(Goods goods) {
		if (goods == null) {
			return null;
		}
		GoodsVO goodsVO = new GoodsVO();
		BeanUtils.copyProperties(goods, goodsVO);
		return goodsVO;
	}
}
