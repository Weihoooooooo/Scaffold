package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.HouseholdPay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 业主缴费信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2023-01-07
 */
@Mapper
@Repository
public interface HouseholdPayMapper extends CommonMapper<HouseholdPay> {
}
