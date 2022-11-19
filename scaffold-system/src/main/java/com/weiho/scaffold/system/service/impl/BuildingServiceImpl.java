package com.weiho.scaffold.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.FileUtils;
import com.weiho.scaffold.common.util.I18nMessagesUtils;
import com.weiho.scaffold.common.util.PageUtils;
import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
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
            map.put("创建时间", building.getCreateTime());
            map.put("修改时间", building.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBuilding(Building resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        Building building = this.getById(resources.getId());

        Building buildingBuildingNum = this.getOne(new LambdaQueryWrapper<Building>().eq(Building::getBuildingNum, resources.getBuildingNum()));
        if (buildingBuildingNum != null && !building.getId().equals(buildingBuildingNum.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("buildingNum.exist.error"));
        }

        building.setBuildingNum(resources.getBuildingNum());
        building.setFloor(resources.getFloor());
        building.setFloorNum(resources.getFloorNum());
        building.setNumber(resources.getFloor() * resources.getFloorNum());

        return this.saveOrUpdate(building);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBuilding(Building resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        Building buildingBuildingNum = this.getOne(new LambdaQueryWrapper<Building>().eq(Building::getBuildingNum, resources.getBuildingNum()));
        if (buildingBuildingNum != null) {
            throw new BadRequestException(I18nMessagesUtils.get("buildingNum.exist.error"));
        }
        // 后台计算总户数
        resources.setNumber(resources.getFloor() * resources.getFloorNum());
        return this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBuilding(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public List<VueSelectVO> getDistinctBuildingSelect() {
        List<VueSelectVO> list = new ArrayList<>();
        QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id, building_num");
        List<Building> buildings = this.getBaseMapper().selectList(queryWrapper);
        for (Building building : buildings) {
            list.add(new VueSelectVO(building.getId(), building.getBuildingNum()));
        }
        return list;
    }
}
