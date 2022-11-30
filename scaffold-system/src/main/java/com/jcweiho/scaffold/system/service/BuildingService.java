package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Building;
import com.jcweiho.scaffold.system.entity.criteria.BuildingQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 楼宇信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-07
 */
public interface BuildingService extends CommonService<Building> {
    /**
     * 查询所有符合条件的楼宇
     *
     * @param criteria 查询条件
     * @return /
     */
    List<Building> findAll(BuildingQueryCriteria criteria);

    /**
     * 查询所有符合条件的楼宇(分页)
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String, Object> getBuildingList(BuildingQueryCriteria criteria, Pageable pageable);

    /**
     * 导出楼宇数据
     *
     * @param response 响应参数
     * @param all      导出的数据
     */
    void download(HttpServletResponse response, List<Building> all) throws IOException;

    /**
     * 修改楼宇信息
     *
     * @param resources 楼宇信息
     * @return /
     */
    boolean updateBuilding(Building resources);

    /**
     * 添加楼宇信息
     *
     * @param resources 楼宇信息
     * @return /
     */
    boolean addBuilding(Building resources);

    /**
     * 删除楼宇信息
     *
     * @param ids 主键数组
     * @return /
     */
    boolean deleteBuilding(Set<Long> ids);

    /**
     * 获取所有的建筑列表
     *
     * @return /
     */
    List<VueSelectVO> getDistinctBuildingSelect();
}
