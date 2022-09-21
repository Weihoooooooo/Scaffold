package com.weiho.scaffold.system.entity.convert;

import com.weiho.scaffold.common.mapstruct.MapStructConvert;
import com.weiho.scaffold.system.entity.SysSetting;
import com.weiho.scaffold.system.entity.vo.SysSettingVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/9/19
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysSettingConvert extends MapStructConvert<SysSetting, SysSettingVO> {
}
