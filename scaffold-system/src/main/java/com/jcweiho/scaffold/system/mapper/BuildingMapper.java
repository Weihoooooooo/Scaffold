package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 楼宇信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-07
 */
@Mapper
@Repository
public interface BuildingMapper extends CommonMapper<Building> {
}
