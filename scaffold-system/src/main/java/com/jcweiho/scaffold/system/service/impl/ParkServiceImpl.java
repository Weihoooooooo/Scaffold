package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.PageUtils;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Park;
import com.jcweiho.scaffold.system.entity.ParkLot;
import com.jcweiho.scaffold.system.entity.convert.ParkVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.ParkQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.ParkVO;
import com.jcweiho.scaffold.system.mapper.ParkMapper;
import com.jcweiho.scaffold.system.service.ParkLotService;
import com.jcweiho.scaffold.system.service.ParkService;
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
 * 车位信息表信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-12-05
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ParkServiceImpl extends CommonServiceImpl<ParkMapper, Park> implements ParkService {
    private final ParkVOConvert parkVOConvert;
    private final ParkLotService parkLotService;

    @Override
    public List<ParkVO> convertToVO(List<Park> parks) {
        List<ParkVO> parkVOList = parkVOConvert.toPojo(parks);
        for (ParkVO parkVO : parkVOList) {
            parkVO.setRegion(parkLotService.getById(parkVO.getParkLotId()).getRegion());
        }
        return parkVOList;
    }

    @Override
    public List<Park> findAll(ParkQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Park.class, criteria)));
    }


    @Override
    public Map<String, Object> findAll(ParkQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Park> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(HttpServletResponse response, List<ParkVO> all) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (ParkVO parkVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("停车场区域", parkVO.getRegion());
            map.put("车位类型", parkVO.getType().getDisplay());
            map.put("是否被购买", parkVO.getIsBuy().getDisplay());
            map.put("车位独立编号", parkVO.getIdentityId());
            map.put("车位价格", parkVO.getPrice());
            map.put("创建时间", parkVO.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePark(ParkVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());

        Park park = this.getById(resources.getId());

        Park parkIdentityId = this.getOne(new LambdaQueryWrapper<Park>().eq(Park::getIdentityId, resources.getIdentityId()));

        if (ObjectUtil.isNotNull(parkIdentityId) && !park.getId().equals(parkIdentityId.getId())) {
            throw new BadRequestException("该停车位编号已存在！");
        }

//        ParkLot parkLot = parkLotService.getById(park.getParkLotId());
//        if (!parkLot.getRegion().equals(resources.getRegion())) {
//            ParkLot parkLot1 = parkLotService.getOne(new LambdaQueryWrapper<ParkLot>().eq(ParkLot::getRegion, resources.getRegion()));
//            if (ObjectUtil.isNull(parkLot1)) {
//                throw new BadRequestException("该停车场区域不存在！");
//            }
//            park.setParkLotId(parkLot1.getId());
//        }
        park.setParkLotId(resources.getParkLotId());
        park.setType(resources.getType());
        park.setIsBuy(resources.getIsBuy());
        park.setIdentityId(resources.getIdentityId());
        park.setPrice(resources.getPrice());

        return this.saveOrUpdate(park);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPark(ParkVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());

        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Park>().eq(Park::getIdentityId, resources.getIdentityId())))) {
            throw new BadRequestException("该停车位编号已存在！");
        }

        Park park = parkVOConvert.toEntity(resources);

        ParkLot parkLot = parkLotService.getById(resources.getParkLotId());
        park.setParkLotId(parkLot.getId());

        return this.save(park);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePark(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
