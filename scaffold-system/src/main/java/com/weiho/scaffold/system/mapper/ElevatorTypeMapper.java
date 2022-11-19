package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.ElevatorType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
@Mapper
@Repository
public interface ElevatorTypeMapper extends CommonMapper<ElevatorType> {
    /**
     * 更具电梯查找电梯的类型
     *
     * @param elevatorId 电梯主键
     * @return 电梯类型
     */
    Set<ElevatorType> findSetByElevatorId(@Param("elevatorId") Long elevatorId);
}
