package com.stephen.popcorn.model.vo;

import com.stephen.popcorn.model.entity.Type;
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
public class TypeVO implements Serializable {
	/**
	 * 商品id
	 */
	private Long id;
	
	/**
	 * 商品名称
	 */
	private String typeName;
	
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
	 * @param typeVO
	 * @return
	 */
	public static Type voToObj(TypeVO typeVO) {
		if (typeVO == null) {
			return null;
		}
		Type goods = new Type();
		BeanUtils.copyProperties(typeVO, goods);
		return goods;
	}
	
	/**
	 * 对象转包装类
	 *
	 * @param goods
	 * @return
	 */
	public static TypeVO objToVo(Type goods) {
		if (goods == null) {
			return null;
		}
		TypeVO typeVO = new TypeVO();
		BeanUtils.copyProperties(goods, typeVO);
		return typeVO;
	}
}
