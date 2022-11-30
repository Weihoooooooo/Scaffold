package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.redis.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

/**
 * @author Weiho
 * @since 2022/8/9
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ScaffoldSystemProperties properties;

    @Test
    void findListByRoles() {
        System.err.println(menuService.findSetByRoleId(6L).stream().filter(i -> i.getParentId() != 0L).collect(Collectors.toList()));
    }
}