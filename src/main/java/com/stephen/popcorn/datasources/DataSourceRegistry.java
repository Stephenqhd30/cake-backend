package com.stephen.popcorn.datasources;

import com.stephen.popcorn.model.enums.GoodsTypeEnum;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.service.GoodsService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: stephen qiu
 * @create: 2024-06-25 18:48
 **/
@Component
public class DataSourceRegistry {
	@Resource
	private GoodsService goodsService;
	
	private Map<String, DataSource<GoodsVO>> typeDataSourceMap;
	
	/**
	 * 初始化的时候执行一次
	 */
	@PostConstruct
	public void doInit() {
		// 注册数据源
		typeDataSourceMap = new HashMap() {{
			put(GoodsTypeEnum.ALL.getValue(), goodsService);
			put(GoodsTypeEnum.ICE_CREAM.getValue(), goodsService);
			put(GoodsTypeEnum.SNACK.getValue(), goodsService);
			put(GoodsTypeEnum.ICE_CREAM.getValue(), goodsService);
			put(GoodsTypeEnum.CHILDREN.getValue(), goodsService);
			put(GoodsTypeEnum.CHILDREN.getValue(), goodsService);
			put(GoodsTypeEnum.METHOD.getValue(), goodsService);
			put(GoodsTypeEnum.CLASSIC.getValue(), goodsService);
			put(GoodsTypeEnum.FESTIVAL.getValue(), goodsService);
			put(GoodsTypeEnum.NOT_AFFORD.getValue(), goodsService);
		}};
	}
	
	public DataSource<GoodsVO> getDataSourceByType(String type) {
		if (typeDataSourceMap == null) {
			return null;
		}
		return typeDataSourceMap.get(type);
	}
}
