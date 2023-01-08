package com.jcweiho.scaffold.system.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.system.entity.Car;
import com.jcweiho.scaffold.system.entity.vo.CarVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022-12-08
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CarVOConvert extends MapStructConvert<Car, CarVO> {
}
