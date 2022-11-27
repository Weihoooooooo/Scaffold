package com.weiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.FileUtils;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.ParkLot;
import com.weiho.scaffold.system.entity.criteria.ParkLotQueryCriteria;
import com.weiho.scaffold.system.mapper.ParkLotMapper;
import com.weiho.scaffold.system.service.ParkLotService;
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
 * 停车场信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-27
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ParkLotServiceImpl extends CommonServiceImpl<ParkLotMapper, ParkLot> implements ParkLotService {
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
        List<Map<String, Object>> list = new ArrayList<>();
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
}
