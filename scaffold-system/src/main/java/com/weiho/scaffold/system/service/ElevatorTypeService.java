package com.weiho.scaffold.system.service;

import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.ElevatorType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
     * 根据电梯查找电梯的类型
     *
     * @param elevatorId 电梯主键
     * @return 电梯类型
     */
    Set<ElevatorType> findSetByElevatorId(Long elevatorId);

    /**
     * 获取电梯的下拉选择框
     *
     * @return 电梯类型列表
     */
    List<VueSelectVO> getElevatorTypeSelect(HttpServletRequest request);
}
