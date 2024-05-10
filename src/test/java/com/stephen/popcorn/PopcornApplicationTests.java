package com.stephen.popcorn;

import com.stephen.popcorn.config.WxOpenConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 主类测试
 *
 * @author stephen qiu
 * 
 */
@SpringBootTest
class PopcornApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }

}
