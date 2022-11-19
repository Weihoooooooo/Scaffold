package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.UsersRoles;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户角色中间表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-09-21
 */
@Mapper
@Repository
public interface UsersRolesMapper extends CommonMapper<UsersRoles> {
}
