package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.entity.Feedback;
import com.jcweiho.scaffold.system.entity.criteria.FeedbackQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.FeedbackVO;
import com.jcweiho.scaffold.system.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class FeedbackController extends CommonController<FeedbackService, Feedback> {
    @ApiOperation("查询所有的反馈列表")
    @GetMapping
    @PreAuthorize("@el.check('Feedback:list')")
    public Map<String, Object> getFeedbackList(@Validated FeedbackQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging(title = "解决反馈", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("解决反馈")
    @PutMapping
    @RateLimiter(limitType = LimitType.IP)
    @PreAuthorize("@el.check('Feedback:update')")
    public Result answerFeedback(@Validated @RequestBody FeedbackVO resources) {
        return resultMessage(Operate.OPERATE, this.getBaseService().answerFeedback(resources));
    }

    @Logging(title = "删除反馈", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除反馈")
    @DeleteMapping
    @PreAuthorize("@el.check('Feedback:delete')")
    public Result deleteFeedback(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteFeedback(filterCollNullAndDecrypt(ids)));
    }

    @Logging("导出反馈信息")
    @ApiOperation("导出反馈信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Feedback:list')")
    public void download(@Validated FeedbackQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)), response);
    }

}
