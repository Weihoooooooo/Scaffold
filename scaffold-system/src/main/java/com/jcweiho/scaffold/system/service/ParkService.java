package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Park;
import com.jcweiho.scaffold.system.entity.criteria.ParkQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.ParkVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 车位信息表信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-12-05
 */
public interface ParkService extends CommonService<Park> {
    /**
     * 将查询结果转为特殊VO
     *
     * @param parks 车位信息表结果
     * @return 转换结果
     */
    List<ParkVO> convertToVO(List<Park> parks);

    /**
     * 查询所有符合条件的车位信息表
     *
     * @param criteria 条件
     * @return 查询结果
     */
    List<Park> findAll(ParkQueryCriteria criteria);

    /**
     * 查询所有符合条件的车位信息表(分页)
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return 分页结果
     */
    Map<String, Object> findAll(ParkQueryCriteria criteria, Pageable pageable);

    /**
     * 下载车位信息表
     *
     * @param response 响应参数
     * @param all      结果集
     */
    void download(HttpServletResponse response, List<ParkVO> all) throws IOException;

    /**
     * 更新车位信息表
     *
     * @param resources 新车位信息表
     * @return 是否成功
     */
    boolean updatePark(ParkVO resources);

    /**
     * 添加车位信息表
     *
     * @param resources 车位信息表
     * @return 是否成功
     */
    boolean addPark(ParkVO resources);

    /**
     * 删除车位信息表
     *
     * @param ids 通知ID
     * @return 是否成功
     */
    boolean deletePark(Set<Long> ids);
}
