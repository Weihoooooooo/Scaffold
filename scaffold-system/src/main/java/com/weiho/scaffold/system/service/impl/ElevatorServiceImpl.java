package com.weiho.scaffold.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.*;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Elevator;
import com.weiho.scaffold.system.entity.ElevatorType;
import com.weiho.scaffold.system.entity.ElevatorsTypes;
import com.weiho.scaffold.system.entity.convert.ElevatorVOConvert;
import com.weiho.scaffold.system.entity.criteria.ElevatorQueryCriteria;
import com.weiho.scaffold.system.entity.vo.ElevatorMaintainVO;
import com.weiho.scaffold.system.entity.vo.ElevatorVO;
import com.weiho.scaffold.system.mapper.ElevatorMapper;
import com.weiho.scaffold.system.service.BuildingService;
import com.weiho.scaffold.system.service.ElevatorService;
import com.weiho.scaffold.system.service.ElevatorTypeService;
import com.weiho.scaffold.system.service.ElevatorsTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private final ElevatorTypeService elevatorTypeService;
    private final ElevatorsTypesService elevatorsTypesService;

    @Override
    public List<Elevator> findAll(ElevatorQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Elevator.class, criteria)));
    }

    @Override
    public List<ElevatorVO> convertToVO(List<Elevator> elevators) {
        List<ElevatorVO> elevatorVOS = elevatorVOConvert.toPojo(elevators);
        for (ElevatorVO elevatorVO : elevatorVOS) {
            elevatorVO.setBuildingNum(buildingService.getById(elevatorVO.getBuildingId()).getBuildingNum());
            elevatorVO.setElevatorTypes(elevatorTypeService.findSetByElevatorId(elevatorVO.getId()));
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
            map.put("上一次维护时间", DateUtils.parseDateToStr(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS, elevatorVO.getLastMaintainTime()));
            map.put("下一次维护时间", DateUtils.parseDateToStr(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS, elevatorVO.getNextMaintainTime()));
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
        IdSecureUtils.verifyIdNotNull(resources.getId());
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
        elevator.setIsComputerRoom(resources.getIsComputerRoom());
        elevator.setHoistwaySize(resources.getHoistwaySize());
        elevator.setDepthOfFoundationPit(resources.getDepthOfFoundationPit());
        elevator.setReservedSizeOfDoorOpening(resources.getReservedSizeOfDoorOpening());
        elevator.setLiftingHeight(resources.getLiftingHeight());
        elevator.setEnabled(resources.isEnabled());
        elevator.setDay(resources.getDay());

        boolean result = this.saveOrUpdate(elevator);

        // 当主表修改成功后再进行电梯类型修改
        if (result) {
            // 旧的电梯类型列表
            Set<Long> oldElevatorIds = elevatorTypeService.findSetByElevatorId(resources.getId()).stream().map(ElevatorType::getId).collect(Collectors.toSet());
            // 新的电梯类型列表
            Set<Long> newElevatorIds = resources.getElevatorTypes().stream().map(ElevatorType::getId).collect(Collectors.toSet());

            if (CollUtils.isCollectionNotEqual(oldElevatorIds, newElevatorIds) && CollUtils.isNotEmpty(resources.getElevatorTypes())) {
                // 删除原有的电梯类型
                elevatorsTypesService.removeById(resources.getId());
                // 更新新的电梯类型
                return elevatorsTypesService.saveBatch(
                        resources.getElevatorTypes().stream().map(i -> {
                            ElevatorsTypes elevatorsTypes = new ElevatorsTypes();
                            elevatorsTypes.setElevatorId(resources.getId());
                            elevatorsTypes.setTypeId(i.getId());
                            return elevatorsTypes;
                        }).collect(Collectors.toList())
                );
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addElevator(ElevatorVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        // 根据电梯的唯一编号查找
        Elevator elevatorIdentityId = this.getOne(new LambdaQueryWrapper<Elevator>().eq(Elevator::getIdentityId, resources.getIdentityId()));
        if (elevatorIdentityId != null) {
            throw new BadRequestException(I18nMessagesUtils.get("elevator.exist.error"));
        }

        resources.setLastMaintainTime(DateUtils.getNowDate());
        resources.setNextMaintainTime(DateUtils.getDateScope(DateUtils.getNowDate(), resources.getDay()));

        boolean result = this.save(elevatorVOConvert.toEntity(resources));

        // 保存成功后再去查询一次获取主键
        if (result && CollUtils.isNotEmpty(resources.getElevatorTypes())) {
            Long elevatorId = this.getOne(new LambdaQueryWrapper<Elevator>().eq(Elevator::getIdentityId, resources.getIdentityId())).getId();
            return elevatorsTypesService.saveBatch(
                    resources.getElevatorTypes().stream().map(i -> {
                        ElevatorsTypes elevatorsTypes = new ElevatorsTypes();
                        elevatorsTypes.setElevatorId(elevatorId);
                        elevatorsTypes.setTypeId(i.getId());
                        return elevatorsTypes;
                    }).collect(Collectors.toList())
            );
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean maintainElevator(ElevatorMaintainVO resources) {
        Elevator elevator = this.getById(resources.getId());
        return this.lambdaUpdate().set(Elevator::getMaintainPeople, resources.getMaintainPeople())
                .set(Elevator::getMaintainPeoplePhone, resources.getMaintainPeoplePhone())
                .set(Elevator::getLastMaintainTime, DateUtils.getNowDate())
                .set(Elevator::getNextMaintainTime, DateUtils.getDateScope(elevator.getLastMaintainTime(), elevator.getDay()))
                .eq(Elevator::getId, resources.getId()).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteElevator(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
