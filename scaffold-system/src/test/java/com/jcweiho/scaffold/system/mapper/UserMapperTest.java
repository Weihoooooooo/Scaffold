package com.jcweiho.scaffold.system.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void findByUsername() {
//        System.err.println(userMapper.findByUsername("root"));
    }
}