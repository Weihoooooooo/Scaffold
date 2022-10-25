package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Mapper
@Repository
public interface UserMapper extends CommonMapper<User> {
    /**
     * 根据用户名查找用户信息
     *
     * @param username 用户名
     * @return 返回的用户信息对象
     */
    User findByUsername(@Param("username") String username);
}
