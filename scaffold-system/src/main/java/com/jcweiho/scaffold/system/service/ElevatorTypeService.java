package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.ElevatorType;
import com.jcweiho.scaffold.system.entity.criteria.ElevatorTypeQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    /**
     * 获取所有的电梯类型列表
     *
     * @return 电梯类型列表
     */
    List<ElevatorType> findAll(ElevatorTypeQueryCriteria criteria);

    /**
     * 获取所有的电梯类型列表 (分页)
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 电梯类型列表
     */
    Map<String, Object> findAll(ElevatorTypeQueryCriteria criteria, Pageable pageable);

    /**
     * 下载电梯类型列表
     *
     * @param all      导出的数据
     * @param response 响应参数
     */
    void download(List<ElevatorType> all, HttpServletResponse response) throws IOException;

    /**
     * 修改电梯类型信息
     *
     * @param resources 电梯类型信息
     * @return 是否成功
     */
    boolean updateElevatorType(ElevatorType resources);

    /**
     * 添加电梯类型信息
     *
     * @param resources 电梯类型信息
     * @return 是否成功
     */
    boolean addElevatorType(ElevatorType resources);

    /**
     * 删除电梯类型信息
     *
     * @param ids 电梯类型主键
     * @return 是否成功
     */
    boolean deleteElevatorType(Set<Long> ids);
}
