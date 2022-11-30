package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.RolesMenus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Weiho
 * @since 2022/9/29
 */
@Mapper
@Repository
public interface RolesMenusMapper extends CommonMapper<RolesMenus> {
}
