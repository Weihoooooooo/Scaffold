package com.jcweiho.scaffold.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcweiho.scaffold.system.security.vo.JwtUserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

/**
 * @author Weiho
 * @since 2022/8/4
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    void mapToGrantedAuthorities() throws JsonProcessingException {
        Collection<SimpleGrantedAuthority> list = roleService.mapToGrantedAuthorities(4L, "root");
        JwtUserVO jwtUserVO = new JwtUserVO();
        jwtUserVO.setAuthorities(list);
        System.err.println(jwtUserVO);
    }
}