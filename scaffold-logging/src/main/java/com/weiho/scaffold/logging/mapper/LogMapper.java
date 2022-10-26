package com.weiho.scaffold.logging.mapper;

import com.weiho.scaffold.logging.entity.Log;
import com.weiho.scaffold.mp.mapper.CommonMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    /**
     * 根据用户名查找操作日志
     *
     * @param username 用户名
     * @param logType  日志类型
     * @return /
     */
    List<Log> selectByUsername(@Param("username") String username, @Param("logType") String logType);
}
