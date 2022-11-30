package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Elevator;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 电梯信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-12
 */
@Mapper
@Repository
public interface ElevatorMapper extends CommonMapper<Elevator> {
}
