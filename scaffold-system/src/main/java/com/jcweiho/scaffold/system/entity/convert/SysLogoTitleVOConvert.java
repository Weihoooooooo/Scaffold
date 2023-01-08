package com.jcweiho.scaffold.system.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.system.entity.SysSetting;
import com.jcweiho.scaffold.system.entity.vo.SysLogoTitleVO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/9/19
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysLogoTitleVOConvert extends MapStructConvert<SysSetting, SysLogoTitleVO> {
}
