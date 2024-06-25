package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.model.dto.goods.GoodsQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.popcorn.model.dto.user.UserQueryRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author stephen qiu
* @description 针对表【goods(商品表)】的数据库操作Service
* @createDate 2024-06-24 17:35:30
*/
public interface GoodsService extends IService<Goods> {
	
	void validGoods(Goods goods, boolean add);
	
	QueryWrapper<Goods> getQueryWrapper(GoodsQueryRequest goodsQueryRequest);
	
	GoodsVO getGoodsVO(Goods goods, HttpServletRequest request);
	
	Page<GoodsVO> getGoodsVOPage(Page<Goods> goodsPage, HttpServletRequest request);
	
	/**
	 * 分页查询商品
	 * @param goodsQueryRequest
	 * @return
	 */
	Page<GoodsVO> listGoodsVOByPage(GoodsQueryRequest goodsQueryRequest);
}
