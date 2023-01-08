package com.jcweiho.scaffold.system.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/8/4
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleDTOConvert extends MapStructConvert<Role, RoleDTO> {
}
