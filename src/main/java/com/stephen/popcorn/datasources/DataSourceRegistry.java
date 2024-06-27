package com.stephen.popcorn.datasources;

import com.stephen.popcorn.model.enums.GoodsTypeEnum;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.service.GoodsService;
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
	private GoodsDataSource goodsDataSource;
	
	private Map<String, DataSource<GoodsVO>> typeDataSourceMap;
	
	/**
	 * 初始化的时候执行一次
	 */
	@PostConstruct
	public void doInit() {
		// 注册数据源
		typeDataSourceMap = new HashMap() {{
			put(GoodsTypeEnum.ICE_CREAM.getValue(), goodsDataSource);
			put(GoodsTypeEnum.SNACK.getValue(), goodsDataSource);
			put(GoodsTypeEnum.ICE_CREAM.getValue(), goodsDataSource);
			put(GoodsTypeEnum.CHILDREN.getValue(), goodsDataSource);
			put(GoodsTypeEnum.CHILDREN.getValue(), goodsDataSource);
			put(GoodsTypeEnum.METHOD.getValue(), goodsDataSource);
			put(GoodsTypeEnum.CLASSIC.getValue(), goodsDataSource);
			put(GoodsTypeEnum.FESTIVAL.getValue(), goodsDataSource);
			put(GoodsTypeEnum.NOT_AFFORD.getValue(), goodsDataSource);
		}};
	}
	
	public DataSource<GoodsVO> getDataSourceByType(String type) {
		if (typeDataSourceMap == null) {
			return null;
		}
		return typeDataSourceMap.get(type);
	}
}
