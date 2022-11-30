package com.jcweiho.scaffold.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Weiho
 * @since 2022/11/14
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BuildingServiceTest {
    @Autowired
    private BuildingService buildingService;

    @Test
    void test() {
        System.err.println(buildingService.getDistinctBuildingSelect());
    }
}