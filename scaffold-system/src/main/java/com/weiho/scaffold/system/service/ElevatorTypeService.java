package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.ElevatorType;

import java.util.Set;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
public interface ElevatorTypeService extends CommonService<ElevatorType> {
    /**
     * 更具电梯查找电梯的类型
     *
     * @param elevatorId 电梯主键
     * @return 电梯类型
     */
    Set<ElevatorType> findSetByElevatorId(Long elevatorId);
}
