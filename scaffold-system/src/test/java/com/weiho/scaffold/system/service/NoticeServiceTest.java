package com.weiho.scaffold.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;

//    @Test
//    void findAll() {
//        NoticeQueryCriteria criteria = new NoticeQueryCriteria();
//        criteria.setType(NoticeToEnum.ALL);
//        System.err.println(noticeService.findAll(criteria));
//    }

    @Test
    void test() {
        System.err.println(noticeService.getDistinctUserSelect());
    }
}