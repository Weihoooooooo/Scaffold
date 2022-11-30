package com.jcweiho.scaffold.common.config.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.result.enums.ResultCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Swagger配置信息
 *
 * @author Weiho
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableKnife4j
@RequiredArgsConstructor
public class SwaggerConfig {
    private final ScaffoldSystemProperties properties;

    /**
     * 构造分组公用的Docket
     *
     * @param enabled            是否开启Swagger文档
     * @param apiInfo            Swagger接口文档信息
     * @param handlerBasePackage Swagger基础扫描路径
     * @return ApiSelectorBuilder
     */
    public ApiSelectorBuilder createCommonDocket(Boolean enabled, ApiInfo apiInfo, String handlerBasePackage) {
        return new Docket(DocumentationType.SWAGGER_2)
                //开启文档
                .enable(enabled)
                .apiInfo(apiInfo)
                .globalResponses(HttpMethod.GET, createResponseCodes())
                .globalResponses(HttpMethod.POST, createResponseCodes())
                .globalResponses(HttpMethod.PUT, createResponseCodes())
                .globalResponses(HttpMethod.DELETE, createResponseCodes())
                .globalResponses(HttpMethod.HEAD, createResponseCodes())
                .globalResponses(HttpMethod.PATCH, createResponseCodes())
                .globalResponses(HttpMethod.OPTIONS, createResponseCodes())
                .globalResponses(HttpMethod.TRACE, createResponseCodes())
                .select()
                //API基础扫描路径
                .apis(RequestHandlerSelectors.basePackage(handlerBasePackage));
    }

    /**
     * 接口文档分组  ->  需要token
     *
     * @return Docket
     */
    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        if (properties.getSwaggerProperties().getEnabled()) {
            log.info("Swagger -> [{}]", "Swagger接口文档分组 '需要token验证' 初始化");
        }
        return createCommonDocket(properties.getSwaggerProperties().getEnabled(), apiInfo(), "com.jcweiho.scaffold")
                //设置接口名称除了"/auth"开头的不需要token
                .paths(PathSelectors.ant("/api/v1/auth/**").negate())
                .build()
                .groupName("需要Token")
                .globalRequestParameters(createHeaderRequired());
    }

    /**
     * 接口文档分组  ->  不需要token
     *
     * @return Docket
     */
    @Bean(value = "publicApi")
    public Docket publicApi() {
        if (properties.getSwaggerProperties().getEnabled()) {
            log.info("Swagger -> [{}]", "Swagger接口文档分组 '不需要token验证' 初始化");
        }
        return createCommonDocket(properties.getSwaggerProperties().getEnabled(), apiInfo(), "com.jcweiho.scaffold")
                //设置接口名称"/auth"开头的不需要token
                .paths(PathSelectors.ant("/api/v1/auth/**"))
                .build()
                .groupName("不需要Token");
    }

    /**
     * 创建接口API文档信息
     *
     * @return Api文档信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getSwaggerProperties().getTitle())
                .description(properties.getSwaggerProperties().getDescription())
                .contact(new Contact("Scaffold By Weiho", "", "970049938@qq.com"))
                .version(properties.getSwaggerProperties().getVersion())
                .build();
    }

    /**
     * 设置自定义状态码
     *
     * @return 状态码列表
     */
    private List<Response> createResponseCodes() {
        List<Response> responseList = ListUtils.list(false);
        for (ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()) {
            responseList.add(new ResponseBuilder().code(String.valueOf(resultCodeEnum.getCode())).description(resultCodeEnum.getMsg()).build());
        }
        return responseList;
    }

    /**
     * 设置token为必填头部
     *
     * @return 请求参数列表
     */
    public List<RequestParameter> createHeaderRequired() {
        List<RequestParameter> parameters = ListUtils.list(false);
        parameters.add(new RequestParameterBuilder().description("token").name(properties.getJwtProperties().getHeader())
                .in(ParameterType.HEADER).required(true)
                .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING))).build());
        return parameters;
    }

}
