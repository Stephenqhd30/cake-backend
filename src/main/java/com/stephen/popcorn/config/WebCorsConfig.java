package com.stephen.popcorn.config;

import com.stephen.popcorn.aop.JWTInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置
 *
 * @author stephen qiu
 */
@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new JWTInterceptor())
				.addPathPatterns("api/**")
				.excludePathPatterns("/user/login", "/user/register", "/user/logout",
						"/swagger-resources/**", "/swagger-ui.html",
						"/v2/api-docs", "/webjars/**", "/doc.html");
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 覆盖所有请求
		registry.addMapping("/**")
				// 允许发送 Cookie
				.allowCredentials(true)
				// 放行哪些域名（必须用 patterns，否则 * 会和 allowCredentials 冲突）
				.allowedOriginPatterns("http://localhost:8000")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.exposedHeaders("*");
	}
}
