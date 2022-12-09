package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Car;
import com.jcweiho.scaffold.system.entity.criteria.CarQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.CarVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业主车辆信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-12-08
 */
public interface CarService extends CommonService<Car> {
    /**
     * 将查询结果转为特殊VO
     *
     * @param cars 业主车辆信息表结果
     * @return 转换结果
     */
    List<CarVO> convertToVO(List<Car> cars);

    /**
     * 查询所有符合条件的业主车辆
     *
     * @param criteria 条件
     * @return 查询结果
     */
    List<Car> findAll(CarQueryCriteria criteria);

    /**
     * 查询所有符合条件的业主车辆(分页)
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return 分页结果
     */
    Map<String, Object> findAll(CarQueryCriteria criteria, Pageable pageable);

    /**
     * 下载业主车辆
     *
     * @param response 响应参数
     * @param all      结果集
     */
    void download(List<CarVO> all, HttpServletResponse response) throws IOException;

    /**
     * 更新业主车辆信息
     *
     * @param resources 新业主车辆信息
     * @return 是否成功
     */
    boolean updateCar(CarVO resources);

    /**
     * 添加业主车辆信息
     *
     * @param resources 业主车辆信息
     * @return 是否成功
     */
    boolean addCar(CarVO resources);

    /**
     * 删除业主车辆信息
     *
     * @param ids 通知ID
     * @return 是否成功
     */
    boolean deleteCar(Set<Long> ids);
}
