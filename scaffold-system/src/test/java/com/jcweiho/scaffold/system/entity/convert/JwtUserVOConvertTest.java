package com.jcweiho.scaffold.system.entity.convert;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.InetAddress;

/**
 * @author Weiho
 * @since 2022/7/29
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtUserVOConvertTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${server.port}")
    private String port;

    @Test
    void jwtUserTest() throws Exception {
//        User user = new User(1L, 1L, "ASD", "ASDASD", SexEnum.MAN, "987@qq.com", "123", true, new Date());
//        JwtUserVO jwtUserVO = jwtUserConvert.toPojo(user);
//        System.out.println(jwtUserVO);
//        System.out.println(new ObjectMapper().writeValueAsString(jwtUserVO));
//        System.out.println(passwordEncoder.encode("admin"));
//        System.out.println(passwordEncoder.encode("staff"));
//        System.out.println(passwordEncoder.encode("defender"));
//        System.out.println(passwordEncoder.encode("root"));
//        System.out.println(DateUtils.getNowDateFormat(FormatEnum.YYYYMMDD) + MD5Utils.getMd5(IdUtil.simpleUUID()));
//        System.out.println(DateUtils.getNowDateFormat(FormatEnum.YYYYMMDD) + MD5Utils.getMd5(IdUtil.simpleUUID()));
//        System.out.println(DateUtils.getNowDateFormat(FormatEnum.YYYYMMDD) + MD5Utils.getMd5(IdUtil.simpleUUID()));
        System.out.println("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
//        File file = new File("E:\\Graduation_ChouChou\\Scaffold\\scaffold-system\\src\\main\\resources\\static\\avatar\\20220730995C17EB052C462DE048D96C5FE83617.png");
//        System.err.println(FileUtils.getSize(file.length()));
    }
}