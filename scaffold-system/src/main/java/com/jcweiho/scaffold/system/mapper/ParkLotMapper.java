package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.ParkLot;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 停车场信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-27
 */
@Mapper
@Repository
public interface ParkLotMapper extends CommonMapper<ParkLot> {
}
