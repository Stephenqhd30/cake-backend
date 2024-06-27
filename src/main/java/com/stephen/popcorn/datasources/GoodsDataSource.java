package com.stephen.popcorn.datasources;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.model.dto.goods.GoodsQueryRequest;
import com.stephen.popcorn.model.dto.search.SearchRequest;
import com.stephen.popcorn.model.dto.user.UserQueryRequest;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.model.vo.GoodsVO;
import com.stephen.popcorn.service.GoodsService;
import com.stephen.popcorn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 帖子服务实现
 *
 * @author stephen qiu
 */
@Service
@Slf4j
public class GoodsDataSource implements DataSource<GoodsVO> {
	@Resource
	private GoodsService goodsService;
	
	@Override
	public Page<GoodsVO> doSearch(String searchText, String type, long pageNum, long pageSize) {
		GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
		goodsQueryRequest.setTypeName(type);
		goodsQueryRequest.setSearchText(searchText);
		goodsQueryRequest.setCurrent((int) pageNum);
		goodsQueryRequest.setPageSize((int) pageSize);
		return goodsService.listGoodsVOByPage(goodsQueryRequest);
	}
}




