package com.jcweiho.scaffold.system.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiho
 * @since 2022/8/4
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    void findByUserId() {
        System.err.println(roleMapper.findSetByUserId(1L));
    }
}