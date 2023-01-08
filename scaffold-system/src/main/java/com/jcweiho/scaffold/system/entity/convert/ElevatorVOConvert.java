package com.jcweiho.scaffold.system.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.system.entity.Elevator;
import com.jcweiho.scaffold.system.entity.vo.ElevatorVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/11/12
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ElevatorVOConvert extends MapStructConvert<Elevator, ElevatorVO> {
}
