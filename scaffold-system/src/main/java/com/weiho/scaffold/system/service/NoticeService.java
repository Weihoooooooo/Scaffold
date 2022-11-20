package com.weiho.scaffold.system.service;

import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Notice;
import com.weiho.scaffold.system.entity.criteria.NoticeQueryCriteria;
import com.weiho.scaffold.system.entity.vo.NoticeVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 通知信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-02
 */
public interface NoticeService extends CommonService<Notice> {
    /**
     * 将查询结果转为特殊VO
     *
     * @param notices 结果
     * @return /
     */
    List<NoticeVO> convertToVO(List<Notice> notices);

    /**
     * 查询所有符合条件的通知
     *
     * @param criteria 条件
     * @return /
     */
    List<Notice> findAll(NoticeQueryCriteria criteria);

    /**
     * 查询所有符合条件的通知(分页)
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return /
     */
    Map<String, Object> getNoticeList(NoticeQueryCriteria criteria, Pageable pageable);

    /**
     * 下载通知
     *
     * @param response 响应参数
     * @param all      结果集
     */
    void download(HttpServletResponse response, List<NoticeVO> all) throws IOException;

    /**
     * 更新通知
     *
     * @param resources 新通知
     * @return /
     */
    boolean updateNotice(NoticeVO resources);

    /**
     * 添加通知
     *
     * @param resources 通知
     * @return /
     */
    boolean addNotice(NoticeVO resources);

    /**
     * 获取发送人去重列表
     *
     * @return /
     */
    List<VueSelectVO> getDistinctUserSelect();

    /**
     * 删除通知
     *
     * @param ids 通知ID
     * @return /
     */
    boolean deleteNotice(Set<Long> ids);
}
