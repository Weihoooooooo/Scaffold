package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Visitor;
import com.jcweiho.scaffold.system.entity.criteria.VisitorQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.VisitorVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 访客信息信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2023-01-02
 */
public interface VisitorService extends CommonService<Visitor> {
    /**
     * 将查询结果转为特殊VO
     *
     * @param visitors 访客信息结果
     * @return 转换结果
     */
    List<VisitorVO> convertToVO(List<Visitor> visitors);

    /**
     * 查询所有符合条件的访客信息
     *
     * @param criteria 条件
     * @return 查询结果
     */
    List<Visitor> findAll(VisitorQueryCriteria criteria);

    /**
     * 查询所有符合条件的访客信息(分页)
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return 分页结果
     */
    Map<String, Object> findAll(VisitorQueryCriteria criteria, Pageable pageable);

    /**
     * 下载访客信息
     *
     * @param response 响应参数
     * @param all      结果集
     */
    void download(List<VisitorVO> all, HttpServletResponse response) throws IOException;

    /**
     * 更新访客信息
     *
     * @param resources 新访客信息
     * @return 是否成功
     */
    boolean updateVisitor(VisitorVO resources);

    /**
     * 添加访客信息
     *
     * @param resources 访客信息
     * @return 是否成功
     */
    boolean addVisitor(VisitorVO resources);

    /**
     * 删除访客信息
     *
     * @param ids 通知ID
     * @return 是否成功
     */
    boolean deleteVisitor(Set<Long> ids);
}
