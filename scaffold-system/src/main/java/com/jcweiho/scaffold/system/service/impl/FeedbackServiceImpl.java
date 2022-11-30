package com.jcweiho.scaffold.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Feedback;
import com.jcweiho.scaffold.system.entity.convert.FeedbackVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.FeedbackQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.FeedbackVO;
import com.jcweiho.scaffold.system.mapper.FeedbackMapper;
import com.jcweiho.scaffold.system.service.FeedbackService;
import com.jcweiho.scaffold.system.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 反馈信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FeedbackServiceImpl extends CommonServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {
    private final FeedbackVOConvert feedbackVOConvert;
    private final OwnerService ownerService;


    @Override
    public List<Feedback> findAll(FeedbackQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Feedback.class, criteria)));
    }

    @Override
    public List<FeedbackVO> convertToVO(List<Feedback> feedbacks) {
        List<FeedbackVO> feedbackVOS = feedbackVOConvert.toPojo(feedbacks);
        for (FeedbackVO feedbackVO : feedbackVOS) {
            feedbackVO.setOwnerName(ownerService.getById(feedbackVO.getOwnerId()).getName());
        }
        return feedbackVOS;
    }

    @Override
    public Map<String, Object> findAll(FeedbackQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Feedback> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean answerFeedback(FeedbackVO resource) {
        IdSecureUtils.verifyIdNotNull(resource.getId());
        Feedback feedback = this.getById(resource.getId());

        feedback.setUsername(SecurityUtils.getUsername());
        feedback.setAnswer(resource.getAnswer());
        feedback.setAnswerTime(DateUtils.getNowDate());
        feedback.setRemarks(resource.getRemarks());
        feedback.setResult(resource.getResult());

        return this.saveOrUpdate(feedback);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFeedback(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public void download(List<FeedbackVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (FeedbackVO feedbackVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.feedback.ownername"), feedbackVO.getOwnerName());
            map.put(I18nMessagesUtils.get("download.feedback.username"), feedbackVO.getUsername());
            map.put(I18nMessagesUtils.get("download.feedback.type"), feedbackVO.getType().getDisplay());
            map.put(I18nMessagesUtils.get("download.feedback.title"), feedbackVO.getTitle());
            map.put(I18nMessagesUtils.get("download.feedback.content"), feedbackVO.getContent());
            map.put(I18nMessagesUtils.get("download.feedback.answer"), feedbackVO.getAnswer());
            map.put(I18nMessagesUtils.get("download.feedback.answer.time"), DateUtils.parseDateToStr(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS, feedbackVO.getAnswerTime()));
            map.put(I18nMessagesUtils.get("download.feedback.answer.result"), feedbackVO.getResult().getDisplay());
            map.put(I18nMessagesUtils.get("download.feedback.remarks"), feedbackVO.getRemarks());
            map.put(I18nMessagesUtils.get("download.feedback.time"), DateUtils.parseDateToStr(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS, feedbackVO.getCreateTime()));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
