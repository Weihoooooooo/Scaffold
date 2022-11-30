package com.jcweiho.scaffold.system.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiho
 * @since 2022/8/4
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuMapperTest {

    @Autowired
    private MenuMapper menuMapper;

    @Test
    void findMenusByRoleId() {
        System.err.println(menuMapper.findSetByRoleId(1L));
    }
}