package com.jcweiho.scaffold.system.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.system.entity.User;
import com.jcweiho.scaffold.system.security.vo.JwtUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * User转JwtUserVO
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JwtUserVOConvert extends MapStructConvert<User, JwtUserVO> {
}
