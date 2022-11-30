package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.PageUtils;
import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Building;
import com.jcweiho.scaffold.system.entity.criteria.BuildingQueryCriteria;
import com.jcweiho.scaffold.system.mapper.BuildingMapper;
import com.jcweiho.scaffold.system.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        List<Map<String, Object>> list = ListUtils.list(false);
        for (Building building : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.building.buildingNum"), building.getBuildingNum());
            map.put(I18nMessagesUtils.get("download.building.floor"), building.getFloor());
            map.put(I18nMessagesUtils.get("download.building.floorNum"), building.getFloorNum());
            map.put(I18nMessagesUtils.get("download.building.num"), building.getNumber());
            map.put(I18nMessagesUtils.get("download.createTime"), building.getCreateTime());
            map.put(I18nMessagesUtils.get("download.updateTime"), building.getUpdateTime());
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
        if (ObjectUtil.isNotNull(buildingBuildingNum)) {
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
        List<VueSelectVO> list = ListUtils.list(false);
        QueryWrapper<Building> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id, building_num");
        List<Building> buildings = this.getBaseMapper().selectList(queryWrapper);
        for (Building building : buildings) {
            list.add(new VueSelectVO(IdSecureUtils.des().encrypt(building.getId()), building.getBuildingNum()));
        }
        return list;
    }
}
