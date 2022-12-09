package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Park;
import com.jcweiho.scaffold.system.entity.ParkLot;
import com.jcweiho.scaffold.system.entity.criteria.ParkLotQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.ParkTypeEnum;
import com.jcweiho.scaffold.system.mapper.ParkLotMapper;
import com.jcweiho.scaffold.system.mapper.ParkMapper;
import com.jcweiho.scaffold.system.service.ParkLotService;
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
 * 停车场信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-27
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ParkLotServiceImpl extends CommonServiceImpl<ParkLotMapper, ParkLot> implements ParkLotService {
    private final ParkMapper parkMapper;

    @Override
    public List<ParkLot> findAll(ParkLotQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(ParkLot.class, criteria)));
    }

    @Override
    public Map<String, Object> findAll(ParkLotQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        return toPageContainer(this.findAll(criteria));
    }

    @Override
    public void download(List<ParkLot> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (ParkLot parkLot : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("停车区域", parkLot.getRegion());
            map.put("小车车位数", parkLot.getNumber());
            map.put("停车场层数", parkLot.getFloor());
            map.put("其他车位数", parkLot.getOtherNumber());
            map.put("是否启用", parkLot.isEnabled() ? '是' : '否');
            map.put(I18nMessagesUtils.get("download.createTime"), parkLot.getCreateTime());
            map.put(I18nMessagesUtils.get("download.updateTime"), parkLot.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addParkLot(ParkLot resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<ParkLot>().eq(ParkLot::getRegion, resources.getRegion())))) {
            throw new BadRequestException("该停车场区域已存在!");
        }
        return this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParkLot(ParkLot resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        ParkLot parkLot = this.getById(resources.getId());

        ParkLot parkLotRegion = this.getOne(new LambdaQueryWrapper<ParkLot>().eq(ParkLot::getRegion, resources.getRegion()));
        if (ObjectUtil.isNotNull(parkLotRegion) && !parkLot.getId().equals(parkLotRegion.getId())) {
            throw new BadRequestException("该停车场区域已存在！");
        }
        parkLot.setRegion(resources.getRegion());
        parkLot.setNumber(resources.getNumber());
        parkLot.setFloor(resources.getFloor());
        parkLot.setOtherNumber(resources.getOtherNumber());
        parkLot.setEnabled(resources.isEnabled());
        return this.saveOrUpdate(parkLot);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteParkLot(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Integer getCountByParkLotAndType(Long parkLotId, ParkTypeEnum type) {
        // 条件构造器
        LambdaQueryWrapper<Park> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Park::getParkLotId, parkLotId).eq(Park::getType, type.getKey());
        return Convert.toInt(parkMapper.selectCount(wrapper));
    }

    @Override
    public List<VueSelectVO> getDistinctRegionSelect() {
        List<VueSelectVO> list = ListUtils.list(false);
        LambdaQueryWrapper<ParkLot> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ParkLot::getId, ParkLot::getRegion);
        List<ParkLot> parkLots = this.getBaseMapper().selectList(wrapper);
        for (ParkLot parkLot : parkLots) {
            // 获取小车车位数量
            Integer typeCarCount = this.getCountByParkLotAndType(parkLot.getId(), ParkTypeEnum.CAR_PARK);
            // 获取其他车位数量
            Integer typeOtherCount = this.getCountByParkLotAndType(parkLot.getId(), ParkTypeEnum.OTHER_PARK);
            // 当车位数量不大于规定的数量才放入数组
            if (typeCarCount <= parkLot.getNumber() && typeOtherCount <= parkLot.getOtherNumber()) {
                list.add(new VueSelectVO(IdSecureUtils.des().encrypt(parkLot.getId()), parkLot.getRegion()));
            }
        }
        return list;
    }
}
