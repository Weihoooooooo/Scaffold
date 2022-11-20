package com.weiho.scaffold.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weiho.scaffold.system.AppRun;
import com.weiho.scaffold.system.service.AvatarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Weiho
 * @since 2022/8/29
 */
@SpringBootTest(classes = {AppRun.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan({"com.weiho.scaffold"})
class LogServiceTest {
    @Autowired
    private AvatarService avatarService;

    @Test
    void findAll() throws JsonProcessingException {
        System.err.println(new ObjectMapper().writeValueAsString(avatarService.getById(4L)));
    }
}