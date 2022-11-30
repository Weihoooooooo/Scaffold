package com.jcweiho.scaffold.system;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.jcweiho.scaffold.common.annotation.Anonymous;
import com.jcweiho.scaffold.common.annotation.NotControllerResponseAdvice;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.i18n.EnableI18n;
import com.jcweiho.scaffold.system.annotation.EnableScaffold;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableAsync
@EnableSwagger2
@EnableI18n
@EnableSpringUtil
@EnableScaffold
@EnableConfigurationProperties(ScaffoldSystemProperties.class)
@Api(tags = "后端健康监测")
@RestController
@MapperScan(basePackages = {"com.jcweiho.scaffold.**.mapper"}) //Mybatis扫描Mapper
@SpringBootApplication
public class AppRun {

    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
    }

    /**
     * 注入TomcatServletWebServer工厂
     */
    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }

    /*
      解决Druid连接池报警告“discard long time none received connection”
     */
    static {
        System.setProperty("druid.mysql.usePingMethod", "false");
    }

    /**
     * 访问首页提示
     *
     * @return /
     */
    @Anonymous
    @GetMapping("/")
    @ApiOperation("后端健康监测")
    @NotControllerResponseAdvice
    public String index() {
        return "Backend service started successfully";
    }
}
