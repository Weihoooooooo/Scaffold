package com.weiho.scaffold.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Building;
import com.weiho.scaffold.system.entity.criteria.BuildingQueryCriteria;
import com.weiho.scaffold.system.mapper.BuildingMapper;
import com.weiho.scaffold.system.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 楼宇信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BuildingServiceImpl extends CommonServiceImpl<BuildingMapper, Building> implements BuildingService {

    @Override
    public List<Building> findAll(BuildingQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Building.class, criteria)));
    }

    @Override
    public Map<String, Object> getBuildingList(BuildingQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Building> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public void download(HttpServletResponse response, List<Building> all) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Building building : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("楼宇栋号", building.getBuildingNum());
            map.put("建筑层数", building.getFloor());
            map.put("一梯几户", building.getFloorNum());
            map.put("总户数", building.getNumber());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBuilding(Building resources) {
        Building building = this.getById(resources.getId());
        building.setBuildingNum(resources.getBuildingNum());
        building.setFloor(resources.getFloor());
        building.setFloorNum(resources.getFloorNum());
        building.setNumber(resources.getNumber());
        return this.saveOrUpdate(building);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBuilding(Building resources) {
        return this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBuilding(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
