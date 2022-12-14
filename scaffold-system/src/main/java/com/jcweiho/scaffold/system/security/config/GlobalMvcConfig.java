package com.jcweiho.scaffold.system.security.config;

import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.sensitive.convert.EnumMvcConverter;
import com.jcweiho.scaffold.common.sensitive.convert.IdDecryptConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置跨域和MVC配置
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class GlobalMvcConfig implements WebMvcConfigurer {
    private final ScaffoldSystemProperties properties;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOriginPattern("*");
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(18000L);
        // 允许提交请求的方法类型，*表示全部允许
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/local/**").addResourceLocations("file:" + properties.getResourcesProperties().getLocalAddressPrefix()).setCachePeriod(0);
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/monitor/**").addResourceLocations("classpath:/META-INF/resources/monitor/");
    }

    /**
     * 枚举类转换器完成 key -> Enum 的转换
     */
    @Bean
    public EnumMvcConverter enumMvcConverterFactory() {
        return new EnumMvcConverter();
    }

    /**
     * 主键解密转换器 当加入 @IdDecrypt 的字段将会被自动解密
     */
    @Bean
    public IdDecryptConverter idDecryptConverter() {
        return new IdDecryptConverter();
    }

    /**
     * 将枚举类转换器注入
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(enumMvcConverterFactory());
        registry.addConverter(idDecryptConverter());
    }
}

