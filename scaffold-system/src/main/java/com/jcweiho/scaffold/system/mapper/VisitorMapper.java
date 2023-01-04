package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Visitor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 访客信息信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2023-01-02
 */
@Mapper
@Repository
public interface VisitorMapper extends CommonMapper<Visitor> {
}
