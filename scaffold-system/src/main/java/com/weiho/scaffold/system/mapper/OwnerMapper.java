package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.Owner;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 业主表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
@Mapper
@Repository
public interface OwnerMapper extends CommonMapper<Owner> {
}
