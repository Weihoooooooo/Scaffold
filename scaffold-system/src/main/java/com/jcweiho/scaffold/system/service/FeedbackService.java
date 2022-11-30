package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Feedback;
import com.jcweiho.scaffold.system.entity.criteria.FeedbackQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.FeedbackVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 反馈信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-04
 */
public interface FeedbackService extends CommonService<Feedback> {
    /**
     * 查找所有符合条件的反馈信息
     *
     * @param criteria 查询条件
     * @return /
     */
    List<Feedback> findAll(FeedbackQueryCriteria criteria);

    /**
     * 将结果转换为特殊VO
     *
     * @param feedbacks 结果
     * @return /
     */
    List<FeedbackVO> convertToVO(List<Feedback> feedbacks);

    /**
     * 查询符合条件的反馈,分页
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String, Object> findAll(FeedbackQueryCriteria criteria, Pageable pageable);

    /**
     * 下载反馈信息
     *
     * @param all      下载的数据
     * @param response 响应参数
     */
    void download(List<FeedbackVO> all, HttpServletResponse response) throws IOException;

    /**
     * 解决反馈
     *
     * @param resource 反馈详细及结果
     * @return /
     */
    boolean answerFeedback(FeedbackVO resource);

    /**
     * 删除反馈
     *
     * @param ids 主键
     * @return /
     */
    boolean deleteFeedback(Set<Long> ids);
}
