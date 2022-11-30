package com.jcweiho.scaffold.system.annotation;

import com.jcweiho.scaffold.common.config.ScaffoldCommonConfiguration;
import com.jcweiho.scaffold.logging.config.ScaffoldLoggingConfiguration;
import com.jcweiho.scaffold.mp.config.ScaffoldMybatisPlusConfiguration;
import com.jcweiho.scaffold.redis.config.ScaffoldRedisConfiguration;
import com.jcweiho.scaffold.tools.config.ScaffoldToolsConfiguration;
import com.jcweiho.scaffold.websocket.config.ScaffoldWebsocketConfiguration;
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
