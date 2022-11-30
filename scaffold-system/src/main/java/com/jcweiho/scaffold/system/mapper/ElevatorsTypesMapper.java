package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.ElevatorsTypes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
@Mapper
@Repository
public interface ElevatorsTypesMapper extends CommonMapper<ElevatorsTypes> {
}
