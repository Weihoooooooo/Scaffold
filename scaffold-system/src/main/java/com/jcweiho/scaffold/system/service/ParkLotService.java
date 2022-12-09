package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.ParkLot;
import com.jcweiho.scaffold.system.entity.criteria.ParkLotQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.ParkTypeEnum;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 停车场信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-27
 */
public interface ParkLotService extends CommonService<ParkLot> {
    /**
     * 查询所有符合条件的停车场列表
     *
     * @param criteria 查询条件
     * @return 停车场列表
     */
    List<ParkLot> findAll(ParkLotQueryCriteria criteria);

    /**
     * 查询所有符合条件的停车场列表(分页)
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 停车场列表
     */
    Map<String, Object> findAll(ParkLotQueryCriteria criteria, Pageable pageable);

    /**
     * 导出停车场列表数据
     *
     * @param all      要导出的数据
     * @param response 响应参数
     */
    void download(List<ParkLot> all, HttpServletResponse response) throws IOException;

    /**
     * 添加停车场信息
     *
     * @param resources 停车场信息
     * @return 是否成功
     */
    boolean addParkLot(ParkLot resources);

    /**
     * 修改停车场信息
     *
     * @param resources 停车场信息
     * @return 是否成功
     */
    boolean updateParkLot(ParkLot resources);

    /**
     * 删除停车场信息
     *
     * @param ids 停车场主键
     * @return 是否成功
     */
    boolean deleteParkLot(Set<Long> ids);

    /**
     * 获取指定停车场ID和指定停车位类型的车位数量
     *
     * @param parkLotId 停车场ID
     * @param type      停车位类型
     * @return 数量
     */
    Integer getCountByParkLotAndType(Long parkLotId, ParkTypeEnum type);

    /**
     * 获取停车区域下拉框
     *
     * @return /
     */
    List<VueSelectVO> getDistinctRegionSelect();
}
