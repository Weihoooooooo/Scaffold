package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.Owner;
import com.jcweiho.scaffold.system.entity.convert.HouseholdVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdVO;
import com.jcweiho.scaffold.system.mapper.HouseholdMapper;
import com.jcweiho.scaffold.system.service.BuildingService;
import com.jcweiho.scaffold.system.service.HouseholdService;
import com.jcweiho.scaffold.system.service.OwnerService;
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
 * 梯户信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-12-13
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HouseholdServiceImpl extends CommonServiceImpl<HouseholdMapper, Household> implements HouseholdService {
    private final HouseholdVOConvert householdVOConvert;
    private final OwnerService ownerService;
    private final BuildingService buildingService;

    @Override
    public List<HouseholdVO> convertToVO(List<Household> households) {
        List<HouseholdVO> householdVOList = householdVOConvert.toPojo(households);
        for (HouseholdVO householdVO : householdVOList) {
            // 业主姓名
            householdVO.setOwnerName(ownerService.getById(householdVO.getOwnerId()).getName());
            // 建筑栋号
            householdVO.setBuildingNum(buildingService.getById(householdVO.getBuildingId()).getBuildingNum());
        }
        return householdVOList;
    }

    @Override
    public List<Household> findAll(HouseholdQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Household.class, criteria)));
    }


    @Override
    public Map<String, Object> findAll(HouseholdQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Household> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(List<HouseholdVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (HouseholdVO householdVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("业主姓名", householdVO.getOwnerName());
            map.put("建筑栋号", householdVO.getBuildingNum());
            map.put("梯户独立编号", householdVO.getIdentityId());
            map.put("面积", householdVO.getArea());
            map.put("水表读数", householdVO.getMeterWater());
            map.put("上一次水表读数", householdVO.getLastMeterWater());
            map.put("电表读数", householdVO.getMeterElectric());
            map.put("上一次电表读数", householdVO.getLastMeterElectric());
            map.put("常住人数", householdVO.getPeopleNumber());
            map.put("是否居住", householdVO.getIsLive().getDisplay());
            map.put("创建时间", householdVO.getCreateTime());
            map.put("修改时间", householdVO.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHousehold(HouseholdVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        Household household = this.getById(resources.getId());
        // 验证户号
        Household householdIdentityId = this.getOne(new LambdaQueryWrapper<Household>()
                .eq(Household::getIdentityId, resources.getIdentityId()));
        if (ObjectUtil.isNotNull(householdIdentityId) && !household.getId().equals(householdIdentityId.getId())) {
            throw new BadRequestException("该户号已被占用！");
        }
        household.setIdentityId(resources.getIdentityId());
        household.setPeopleNumber(resources.getPeopleNumber());
        household.setIsLive(resources.getIsLive());

        // 修改上一次的水表读数
        household.setLastMeterWater(household.getMeterWater());
        // 放入本次修改的水表读数
        household.setMeterWater(resources.getMeterWater());

        // 修改上一次的电表读数
        household.setLastMeterElectric(household.getMeterElectric());
        // 放入本次修改的电表读数
        household.setMeterElectric(resources.getMeterElectric());

        return this.saveOrUpdate(household);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addHousehold(HouseholdVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        if (StringUtils.isBlank(resources.getPhone())) {
            throw new BadRequestException("业主手机不能为空！");
        }
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Household>().eq(Household::getIdentityId, resources.getIdentityId())))) {
            throw new BadRequestException("该户号已被占用！");
        }
        Household household = householdVOConvert.toEntity(resources);
        Long ownerId = ownerService.getOne(new LambdaQueryWrapper<Owner>()
                .eq(Owner::getPhone, LikeCipherUtils.phoneLikeEncrypt(resources.getPhone()))).getId();
        household.setOwnerId(ownerId);
        // 手动设置上一次的水电读数
        household.setLastMeterElectric(0.00);
        household.setLastMeterWater(0.00);
        return this.save(household);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHousehold(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
