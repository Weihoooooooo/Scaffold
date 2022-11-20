package com.weiho.scaffold.system.annotation;

import com.weiho.scaffold.common.config.ScaffoldCommonConfiguration;
import com.weiho.scaffold.logging.config.ScaffoldLoggingConfiguration;
import com.weiho.scaffold.mp.config.ScaffoldMybatisPlusConfiguration;
import com.weiho.scaffold.redis.config.ScaffoldRedisConfiguration;
import com.weiho.scaffold.tools.config.ScaffoldToolsConfiguration;
import com.weiho.scaffold.websocket.config.ScaffoldWebsocketConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Weiho
 * @since 2022/11/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({
        ScaffoldCommonConfiguration.class,
        ScaffoldLoggingConfiguration.class,
        ScaffoldRedisConfiguration.class,
        ScaffoldToolsConfiguration.class,
        ScaffoldWebsocketConfiguration.class,
        ScaffoldMybatisPlusConfiguration.class
})
public @interface EnableScaffold {
}
