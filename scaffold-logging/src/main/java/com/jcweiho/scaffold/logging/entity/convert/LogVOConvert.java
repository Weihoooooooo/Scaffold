package com.jcweiho.scaffold.logging.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.logging.entity.vo.LogVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/8/29
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogVOConvert extends MapStructConvert<Log, LogVO> {
}
