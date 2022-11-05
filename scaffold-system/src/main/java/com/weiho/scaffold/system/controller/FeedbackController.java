package com.weiho.scaffold.system.controller;


import com.weiho.scaffold.common.util.enums.EnumSelectVO;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.ResultUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.system.entity.criteria.FeedbackQueryCriteria;
import com.weiho.scaffold.system.entity.vo.FeedbackVO;
import com.weiho.scaffold.system.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 反馈信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-04
 */
@Api(tags = "反馈管理")
@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @ApiOperation("查询所有的反馈列表")
    @GetMapping
    @PreAuthorize("@el.check('Feedback:list')")
    public Map<String, Object> getFeedbackList(FeedbackQueryCriteria criteria, Pageable pageable) {
        return feedbackService.findAll(criteria, pageable);
    }

    @Logging(title = "解决反馈", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("解决反馈")
    @PutMapping
    @RateLimiter(limitType = LimitType.IP)
    @PreAuthorize("@el.check('Feedback:update')")
    public Result answerFeedback(@Validated @RequestBody FeedbackVO resources) {
        return ResultUtils.operateMessage(feedbackService.answerFeedback(resources));
    }

    @Logging(title = "删除反馈", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除反馈")
    @DeleteMapping
    @PreAuthorize("@el.check('Feedback:delete')")
    public Result deleteFeedback(@RequestBody Set<Long> ids) {
        return ResultUtils.deleteMessage(ids, feedbackService.deleteFeedback(ids));
    }

    @Logging(title = "导出反馈信息")
    @ApiOperation("导出反馈信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Feedback:list')")
    public void download(FeedbackQueryCriteria criteria, HttpServletResponse response) throws IOException {
        feedbackService.download(feedbackService.convertToVO(feedbackService.findAll(criteria)), response);
    }

    @ApiOperation("获取反馈处理结果枚举列表")
    @GetMapping("/feedbackResult")
    @PreAuthorize("@el.check('Feedback:list')")
    @RateLimiter(limitType = LimitType.IP)
    public List<EnumSelectVO> getFeedbackResultSelectList() {
        return feedbackService.getFeedbackResultSelect();
    }

    @ApiOperation("获取反馈类型枚举列表")
    @GetMapping("/feedbackTypes")
    @PreAuthorize("@el.check('Feedback:list')")
    @RateLimiter(limitType = LimitType.IP)
    public List<EnumSelectVO> getFeedbackTypeSelectList() {
        return feedbackService.getFeedbackTypeSelect();
    }

}
