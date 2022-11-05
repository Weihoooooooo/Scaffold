package com.weiho.scaffold.system.mapper;

import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.system.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 反馈信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-04
 */
@Mapper
@Repository
public interface FeedbackMapper extends CommonMapper<Feedback> {
}
