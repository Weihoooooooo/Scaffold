package com.jcweiho.scaffold.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.PageUtils;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.HouseholdPay;
import com.jcweiho.scaffold.system.entity.convert.HouseholdPayVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdPayQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdPayVO;
import com.jcweiho.scaffold.system.mapper.HouseholdPayMapper;
import com.jcweiho.scaffold.system.service.BuildingService;
import com.jcweiho.scaffold.system.service.HouseholdPayService;
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
 * 业主缴费信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2023-01-07
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class HouseholdPayServiceImpl extends CommonServiceImpl<HouseholdPayMapper, HouseholdPay> implements HouseholdPayService {
    private final HouseholdPayVOConvert householdPayVOConvert;
    private final HouseholdService householdService;
    private final BuildingService buildingService;
    private final OwnerService ownerService;

    @Override
    public List<HouseholdPayVO> convertToVO(List<HouseholdPay> householdPays) {
        List<HouseholdPayVO> householdPayVOList = householdPayVOConvert.toPojo(householdPays);
        for (HouseholdPayVO householdPayVO : householdPayVOList) {
            Household household = householdService.getById(householdPayVO.getHouseholdId());
            householdPayVO.setHousehold(household);
            householdPayVO.setBuildingNum(buildingService.getById(household.getBuildingId()).getBuildingNum());
            householdPayVO.setOwnerName(ownerService.getById(household.getOwnerId()).getName());
        }
        return householdPayVOList;
    }

    @Override
    public List<HouseholdPay> findAll(HouseholdPayQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(HouseholdPay.class, criteria)));
    }


    @Override
    public Map<String, Object> findAll(HouseholdPayQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<HouseholdPay> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(List<HouseholdPayVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (HouseholdPayVO householdPayVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("业主", householdPayVO.getOwnerName());
            map.put("楼栋", householdPayVO.getBuildingNum());
            map.put("梯户", householdPayVO.getHousehold().getIdentityId());
            map.put("水费", householdPayVO.getPayWater());
            map.put("电费", householdPayVO.getPayElectric());
            map.put("年份", householdPayVO.getYear());
            map.put("月份", householdPayVO.getMonth());
            map.put("物管费", householdPayVO.getPayProperty());
            map.put("是否支付水费", householdPayVO.getIsPayWater().getDisplay());
            map.put("是否支付电费", householdPayVO.getIsPayElectric().getDisplay());
            map.put("是否支付物管费", householdPayVO.getIsPayProperty().getDisplay());
            map.put(I18nMessagesUtils.get("download.createTime"), householdPayVO.getCreateTime());
            map.put(I18nMessagesUtils.get("download.updateTime"), householdPayVO.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addHouseholdPay(HouseholdPay resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        return this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHouseholdPay(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
