package com.jcweiho.scaffold.system.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.vo.HouseholdVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022-12-13
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HouseholdVOConvert extends MapStructConvert<Household, HouseholdVO> {
}
