package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
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
import java.util.*;

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
            map.put(I18nMessagesUtils.get("download.owner.name"), householdVO.getOwnerName());
            map.put(I18nMessagesUtils.get("download.elevator.buildingNum"), householdVO.getBuildingNum());
            map.put(I18nMessagesUtils.get("download.household.identity.id"), householdVO.getIdentityId());
            map.put(I18nMessagesUtils.get("download.household.area"), householdVO.getArea());
            map.put(I18nMessagesUtils.get("download.household.meter.water"), householdVO.getMeterWater());
            map.put(I18nMessagesUtils.get("download.household.last.meter.water"), householdVO.getLastMeterWater());
            map.put(I18nMessagesUtils.get("download.household.meter.electric"), householdVO.getMeterElectric());
            map.put(I18nMessagesUtils.get("download.household.last.meter.electric"), householdVO.getLastMeterElectric());
            map.put(I18nMessagesUtils.get("download.household.people.number"), householdVO.getPeopleNumber());
            map.put(I18nMessagesUtils.get("download.household.isLive"), householdVO.getIsLive().getDisplay());
            map.put(I18nMessagesUtils.get("download.createTime"), householdVO.getCreateTime());
            map.put(I18nMessagesUtils.get("download.updateTime"), householdVO.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateHousehold(HouseholdVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        Household household = this.getById(resources.getId());
        // 验证户号
        Household householdIdentityId = this.getOne(new LambdaQueryWrapper<Household>()
                .eq(Household::getIdentityId, resources.getIdentityId()));
        if (ObjectUtil.isNotNull(householdIdentityId) && !household.getId().equals(householdIdentityId.getId())) {
            throw new BadRequestException("该户号已被占用！");
        }

        if (resources.getMeterWater() < household.getMeterWater() || resources.getMeterElectric() < household.getMeterWater()) {
            throw new BadRequestException("填入的水表读数和电表读数不能小于上一次读数");
        }

        household.setIdentityId(resources.getIdentityId());
        household.setPeopleNumber(resources.getPeopleNumber());
        household.setIsLive(resources.getIsLive());

        Map<String, Object> result = new HashMap<>();
        result.put("flag", this.saveOrUpdate(household));
        result.put("household", household);
        return result;
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
        return this.save(household);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHousehold(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public List<VueSelectVO> getDistinctHouseholdSelect(Long buildingId) {
        List<VueSelectVO> list = ListUtils.list(false);
        LambdaQueryWrapper<Household> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Household::getId, Household::getIdentityId)
                .eq(Household::getBuildingId, buildingId);
        List<Household> households = this.getBaseMapper().selectList(wrapper);
        for (Household household : households) {
            list.add(new VueSelectVO(IdSecureUtils.des().encrypt(household.getId()), household.getIdentityId()));
        }
        return list;
    }
}
