package com.stephen.popcorn.model.vo;

import com.stephen.popcorn.model.entity.Recommend;
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
public class RecommendVO implements Serializable {
	/**
	 * 推荐栏id
	 */
	private Long id;
	
	/**
	 * 商品类别
	 */
	private String goodsType;
	
	/**
	 * 商品id
	 */
	private GoodsVO goodsVO;
	
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
	 * @param recommendVO
	 * @return
	 */
	public static Recommend voToObj(RecommendVO recommendVO) {
		if (recommendVO == null) {
			return null;
		}
		Recommend recommend = new Recommend();
		BeanUtils.copyProperties(recommendVO, recommend);
		return recommend;
	}
	
	/**
	 * 对象转包装类
	 *
	 * @param recommend
	 * @return
	 */
	public static RecommendVO objToVo(Recommend recommend) {
		if (recommend == null) {
			return null;
		}
		RecommendVO recommendVO = new RecommendVO();
		BeanUtils.copyProperties(recommend, recommendVO);
		return recommendVO;
	}
}
