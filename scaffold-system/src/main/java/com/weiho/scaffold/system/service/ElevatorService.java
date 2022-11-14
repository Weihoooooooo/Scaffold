package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Elevator;
import com.weiho.scaffold.system.entity.criteria.ElevatorQueryCriteria;
import com.weiho.scaffold.system.entity.vo.ElevatorVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 电梯信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-12
 */
public interface ElevatorService extends CommonService<Elevator> {
    /**
     * 查找符合条件的电梯
     *
     * @param criteria 查询条件
     * @return /
     */
    List<Elevator> findAll(ElevatorQueryCriteria criteria);

    /**
     * 将查询结果转化为特殊VO
     *
     * @param elevators 查询条件
     * @return /
     */
    List<ElevatorVO> convertToVO(List<Elevator> elevators);

    /**
     * 导出电梯数据
     *
     * @param all      导出的数据
     * @param response 响应参数
     */
    void download(List<ElevatorVO> all, HttpServletResponse response);
}
