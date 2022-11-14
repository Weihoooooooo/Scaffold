package com.weiho.scaffold.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.date.DateUtils;
import com.weiho.scaffold.common.util.date.FormatEnum;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Elevator;
import com.weiho.scaffold.system.entity.convert.ElevatorVOConvert;
import com.weiho.scaffold.system.entity.criteria.ElevatorQueryCriteria;
import com.weiho.scaffold.system.entity.vo.ElevatorVO;
import com.weiho.scaffold.system.mapper.ElevatorMapper;
import com.weiho.scaffold.system.service.BuildingService;
import com.weiho.scaffold.system.service.ElevatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 电梯信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-12
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class, readOnly = true)
public class ElevatorServiceImpl extends CommonServiceImpl<ElevatorMapper, Elevator> implements ElevatorService {
    private final ElevatorVOConvert elevatorVOConvert;
    private final BuildingService buildingService;

    @Override
    public List<Elevator> findAll(ElevatorQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Elevator.class, criteria)));
    }

    @Override
    public List<ElevatorVO> convertToVO(List<Elevator> elevators) {
        List<ElevatorVO> elevatorVOS = elevatorVOConvert.toPojo(elevators);
        for (ElevatorVO elevatorVO : elevatorVOS) {
            elevatorVO.setBuildingNum(buildingService.getById(elevatorVO.getBuildingId()).getBuildingNum());
        }
        return elevatorVOS;
    }

    @Override
    public void download(List<ElevatorVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ElevatorVO elevatorVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("建筑栋号", elevatorVO.getBuildingNum());
            map.put("电梯独立编号", elevatorVO.getIdentityId());
            map.put("核载人数", elevatorVO.getNumberOfPeople());
            map.put("核载重量", elevatorVO.getNumberOfWeight());
            map.put("上一次维护时间", DateUtils.parseDateToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, elevatorVO.getLastMaintainTime()));
            map.put("下一次维护时间", DateUtils.parseDateToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, elevatorVO.getNextMaintainTime()));
            map.put("相隔多少天维护一次", elevatorVO.getDay());
            map.put("维护人姓名", elevatorVO.getMaintainPeople());
            map.put("维护人电话号码", elevatorVO.getMaintainPeoplePhone());
            map.put("是否启用", elevatorVO.isEnabled() ? "是" : "否");
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    public Map<String, Object> getElevatorList(ElevatorQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Elevator> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateElevator(ElevatorVO resources) {
        Elevator elevator = this.getById(resources.getId());

        // 根据电梯的唯一编号查找
        Elevator elevatorIdentityId = this.getOne(new LambdaQueryWrapper<Elevator>().eq(Elevator::getIdentityId, resources.getIdentityId()));
        if (elevatorIdentityId != null && !elevator.getId().equals(elevatorIdentityId.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("elevator.exist.error"));
        }

        elevator.setIdentityId(resources.getIdentityId());
        elevator.setBuildingId(resources.getBuildingId());
        elevator.setNumberOfPeople(resources.getNumberOfPeople());
        elevator.setNumberOfWeight(resources.getNumberOfWeight());
        elevator.setEnabled(resources.isEnabled());

        return this.saveOrUpdate(elevator);
    }
}
