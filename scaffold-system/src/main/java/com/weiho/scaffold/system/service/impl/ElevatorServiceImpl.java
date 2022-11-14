package com.weiho.scaffold.system.service.impl;

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
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    public void download(List<ElevatorVO> all, HttpServletResponse response) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ElevatorVO elevatorVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();

        }
    }
}
