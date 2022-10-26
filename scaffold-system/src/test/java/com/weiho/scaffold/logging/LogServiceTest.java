package com.weiho.scaffold.logging;

import com.weiho.scaffold.logging.entity.criteria.LogQueryCriteria;
import com.weiho.scaffold.logging.mapper.LogMapper;
import com.weiho.scaffold.logging.service.LogService;
import com.weiho.scaffold.system.AppRun;
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
    private LogService logService;

    @Autowired
    private LogMapper logMapper;

    @Test
    void findAll() {
        LogQueryCriteria criteria = new LogQueryCriteria();
        criteria.setLogType("ERROR");
        logService.findAll(criteria).forEach(System.err::println);
    }

    @Test
    void findByErrorDetail() {
        System.err.println(logService.findByErrorDetail(79L));
    }
}