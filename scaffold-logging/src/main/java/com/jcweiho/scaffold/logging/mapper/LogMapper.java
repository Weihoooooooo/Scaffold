package com.jcweiho.scaffold.logging.mapper;

import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-08-06
 */
@Mapper
@Repository
public interface LogMapper extends CommonMapper<Log> {
}
