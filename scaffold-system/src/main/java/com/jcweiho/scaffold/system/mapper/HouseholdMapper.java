package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Household;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 梯户信息表信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-12-13
 */
@Mapper
@Repository
public interface HouseholdMapper extends CommonMapper<Household> {
}
