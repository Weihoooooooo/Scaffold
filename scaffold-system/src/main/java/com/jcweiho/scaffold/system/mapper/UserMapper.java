package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
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
}
