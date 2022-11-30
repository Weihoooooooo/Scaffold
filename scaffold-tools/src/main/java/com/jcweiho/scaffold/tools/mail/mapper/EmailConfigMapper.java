package com.jcweiho.scaffold.tools.mail.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.tools.mail.entity.EmailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-09-05
 */
@Mapper
@Repository
public interface EmailConfigMapper extends CommonMapper<EmailConfig> {
}
