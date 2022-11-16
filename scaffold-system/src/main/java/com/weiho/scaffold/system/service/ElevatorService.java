package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Elevator;
import com.weiho.scaffold.system.entity.criteria.ElevatorQueryCriteria;
import com.weiho.scaffold.system.entity.vo.ElevatorMaintainVO;
import com.weiho.scaffold.system.entity.vo.ElevatorVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @return 电梯列表(不带分页)
     */
    List<Elevator> findAll(ElevatorQueryCriteria criteria);

    /**
     * 将查询结果转化为特殊VO
     *
     * @param elevators 查询条件
     * @return 电梯列表, 带楼宇栋号
     */
    List<ElevatorVO> convertToVO(List<Elevator> elevators);

    /**
     * 导出电梯数据
     *
     * @param all      导出的数据
     * @param response 响应参数
     */
    void download(List<ElevatorVO> all, HttpServletResponse response) throws IOException;

    /**
     * 查找符合条件的电梯，分页
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 电梯列表, 带分页
     */
    Map<String, Object> getElevatorList(ElevatorQueryCriteria criteria, Pageable pageable);

    /**
     * 修改电梯信息
     *
     * @param resources 电梯信息
     * @return 是否修改成功
     */
    boolean updateElevator(ElevatorVO resources);

    /**
     * 添加电梯信息
     *
     * @param resources 电梯信息
     * @return 是否添加成功
     */
    boolean addElevator(ElevatorVO resources);

    /**
     * 维护电梯
     *
     * @param resources 电梯信息
     * @return 是否维护成功
     */
    boolean maintainElevator(ElevatorMaintainVO resources);

    /**
     * 删除电梯
     *
     * @param ids 电梯ID
     * @return 是否删除成功
     */
    boolean deleteElevator(Set<Long> ids);
}
