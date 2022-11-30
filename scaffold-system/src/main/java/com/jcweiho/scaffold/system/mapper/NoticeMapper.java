package com.jcweiho.scaffold.system.mapper;

import com.jcweiho.scaffold.mp.mapper.CommonMapper;
import com.jcweiho.scaffold.system.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 通知信息表 Mapper 接口
 * </p>
 *
 * @author Weiho
 * @since 2022-11-02
 */
@Mapper
@Repository
public interface NoticeMapper extends CommonMapper<Notice> {

}
