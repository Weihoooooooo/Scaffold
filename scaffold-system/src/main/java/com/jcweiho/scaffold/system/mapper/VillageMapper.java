package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Village;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 小区基本信息参数 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2023-01-04
 */
@Mapper
@Repository
public interface VillageMapper extends CommonMapper<Village> {
}
