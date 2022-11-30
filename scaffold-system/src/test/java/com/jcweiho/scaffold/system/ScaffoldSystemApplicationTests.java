package com.jcweiho.scaffold.system;

import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScaffoldSystemApplicationTests {

    @Autowired
    private ScaffoldSystemProperties properties;

    @Test
    void contextLoads() {
        System.out.println(properties.getResourcesProperties().getAvatarLocalAddressPrefix());
        System.out.println(properties.getResourcesProperties().getLocalAddressPrefix());
        System.out.println(properties.getResourcesProperties().getAvatarServerAddressPrefix());
    }

}
