package com.jcweiho.scaffold.common.config.swagger;

import com.alibaba.fastjson2.TypeReference;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * 将Pageable转换展示在Swagger中
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerDataConfig {
    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                Type originalType = new TypeReference<List<Timestamp>>() {
                }.getType();
                Type alternateType = new TypeReference<List<Date>>() {
                }.getType();
                return newArrayList(
                        newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)),
                        newRule(resolver.resolve(Timestamp.class), resolver.resolve(Date.class)),
                        newRule(originalType, alternateType)
                );
            }
        };
    }

    @ApiModel
    @Data
    private static class Page {
        @ApiModelProperty("页码 (0-N)")
        private Integer page;

        @ApiModelProperty("每页显示的数目")
        private Integer size;

        @ApiModelProperty("以下列格式排序标准{property,[asc | desc]},默认排序顺序为升序。 " +
                "多种排序条件,需要再起一个参数sort=property[asc | desc],不填则默认按照id升序[id,asc]")
        private List<String> sort;
    }
}
