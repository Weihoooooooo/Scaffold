package com.jcweiho.scaffold.tools.mail.entity.convert;

import com.jcweiho.scaffold.common.mapstruct.MapStructConvert;
import com.jcweiho.scaffold.tools.mail.entity.EmailConfig;
import com.jcweiho.scaffold.tools.mail.entity.vo.EmailConfigVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Weiho
 * @since 2022/9/5
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailConfigVOConvert extends MapStructConvert<EmailConfig, EmailConfigVO> {
}
