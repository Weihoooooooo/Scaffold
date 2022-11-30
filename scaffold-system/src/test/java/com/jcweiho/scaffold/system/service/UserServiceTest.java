package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.system.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiho
 * @since 2022/8/4
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void getBaseJwtUserVO() {
        User user = new User();
        user.setUsername("root");
        System.out.println(userService.getBaseJwtUserVO(user));
    }

    @Test
    void updateEmail() {
        userService.list().forEach(System.err::println);
    }
}