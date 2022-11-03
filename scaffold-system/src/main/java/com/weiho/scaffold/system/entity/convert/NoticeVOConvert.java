package com.weiho.scaffold.system.entity.convert;

import com.weiho.scaffold.common.mapstruct.MapStructConvert;
import com.weiho.scaffold.system.entity.Notice;
import com.weiho.scaffold.system.entity.vo.NoticeVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticeVOConvert extends MapStructConvert<Notice, NoticeVO> {
}
