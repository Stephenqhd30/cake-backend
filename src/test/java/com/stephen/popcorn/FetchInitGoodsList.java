package com.stephen.popcorn;

import cn.hutool.json.JSONUtil;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.model.entity.Goods;
import com.stephen.popcorn.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 获取初始化文章列表
 *
 * @author stephen qiu
 */
@Component
@Slf4j
@SpringBootTest
public class FetchInitGoodsList {
	
	@Resource
	private GoodsService goodsService;
	
	@Test
	public void run() {
		int pageSize = 10;  // 每页获取的图片数量
		int pageNum = 3;  // 页码
		String url = String.format("https://cn.bing.com/images/search?q=%s&first=%d", "高端蛋糕", pageNum);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();  // 连接并获取页面内容
		} catch (IOException e) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");  // 异常处理
		}
		Elements elements = doc.select(".iuscp.isv");  // 选择图片元素
		List<Goods> goodsList = new ArrayList<>();
		Random random = new Random();
		for (Element element : elements) {
			// 获取图片地址
			String m = element.select(".iusc").get(0).attr("m");
			Map<String, Object> map = JSONUtil.toBean(m, Map.class);
			String murl = (String) map.get("murl");
			// 获取标题
			String title = element.select(".inflnk").get(0).attr("aria-label");
			Goods goods = new Goods();
			goods.setGoodsName(title);
			goods.setContent(title);
			goods.setGoodsCover(murl);
			goods.setGoodsImage1(murl);
			goods.setGoodsImage2(murl);
			goods.setUserId(1805210020084367362L);
			goods.setPrice((double) (random.nextInt(100) + 1));  // 生成一个随机价格
			goods.setStock(random.nextInt(100) + 1);  // 库存
			goods.setTypeName("买不起系列");  // 固定类型名称
			goodsList.add(goods);
			if (goodsList.size() >= pageSize) {
				break;
			}
		}
		// 数据入库
		boolean b = goodsService.saveBatch(goodsList);
		if (b) {
			log.info("数据成功保存");
		} else {
			log.error("数据保存失败");
		}
	}
}