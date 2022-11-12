package com.weiho.scaffold.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.util.date.DateUtils;
import com.weiho.scaffold.common.util.date.FormatEnum;
import com.weiho.scaffold.common.util.enums.EnumSelectVO;
import com.weiho.scaffold.common.util.enums.EnumUtils;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.page.PageUtils;
import com.weiho.scaffold.common.util.security.SecurityUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Feedback;
import com.weiho.scaffold.system.entity.convert.FeedbackVOConvert;
import com.weiho.scaffold.system.entity.criteria.FeedbackQueryCriteria;
import com.weiho.scaffold.system.entity.enums.FeedbackResultEnum;
import com.weiho.scaffold.system.entity.enums.FeedbackTypeEnum;
import com.weiho.scaffold.system.entity.vo.FeedbackVO;
import com.weiho.scaffold.system.mapper.FeedbackMapper;
import com.weiho.scaffold.system.service.FeedbackService;
import com.weiho.scaffold.system.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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
    public List<EnumSelectVO> getFeedbackResultSelect() {
        return EnumUtils.getEnumSelect(FeedbackResultEnum.class);
    }

    @Override
    public List<EnumSelectVO> getFeedbackTypeSelect() {
        return EnumUtils.getEnumSelect(FeedbackTypeEnum.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean answerFeedback(FeedbackVO resource) {
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
        List<Map<String, Object>> list = new ArrayList<>();
        for (FeedbackVO feedbackVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("业主姓名", feedbackVO.getOwnerName());
            map.put("处理人", feedbackVO.getUsername());
            map.put("反馈类型", feedbackVO.getType().getDisplay());
            map.put("反馈标题", feedbackVO.getTitle());
            map.put("反馈内容", feedbackVO.getContent());
            map.put("反馈回复", feedbackVO.getAnswer());
            map.put("回复时间", DateUtils.parseDateToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, feedbackVO.getAnswerTime()));
            map.put("反馈结果", feedbackVO.getResult().getDisplay());
            map.put("备注", feedbackVO.getRemarks());
            map.put("反馈时间", DateUtils.parseDateToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, feedbackVO.getCreateTime()));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }
}
