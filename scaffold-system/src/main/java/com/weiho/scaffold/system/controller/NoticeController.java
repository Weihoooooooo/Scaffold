package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.enums.EnumSelectVO;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.system.entity.Notice;
import com.weiho.scaffold.system.entity.criteria.NoticeQueryCriteria;
import com.weiho.scaffold.system.entity.vo.NoticeVO;
import com.weiho.scaffold.system.service.NoticeService;
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
 * 通知信息表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-11-02
 */
@Api(tags = "通知管理")
@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
public class NoticeController extends CommonController<NoticeService, Notice> {
    @ApiOperation("查询通知列表")
    @GetMapping
    @PreAuthorize("@el.check('Notice:use')")
    public Map<String, Object> getNoticeList(@Validated NoticeQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().getNoticeList(criteria, pageable);
    }

    @Logging(title = "导出通知信息")
    @ApiOperation("导出通知信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Notice:use')")
    public void download(HttpServletResponse response, @Validated NoticeQueryCriteria criteria) throws IOException {
        this.getBaseService().download(response, this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)));
    }

    @Logging(title = "添加通知信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加通知信息")
    @PostMapping
    @PreAuthorize("@el.check('Notice:add')")
    public Result addNotice(@Validated @RequestBody NoticeVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addNotice(resources));
    }

    @Logging(title = "修改通知信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改通知信息")
    @PutMapping
    @PreAuthorize("@el.check('Notice:update')")
    public Result updateNotice(@Validated @RequestBody NoticeVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateNotice(resources));
    }

    @Logging(title = "删除通知信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除通知信息")
    @DeleteMapping
    @PreAuthorize("@el.check('Notice:delete')")
    public Result deleteNotice(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteNotice(filterCollNullAndDecrypt(ids)));
    }

    @ApiOperation("获取通知发送范围列表")
    @GetMapping("/noticeScope")
    @PreAuthorize("@el.check('Notice:use')")
    @RateLimiter(limitType = LimitType.IP)
    public List<EnumSelectVO> getNoticeTypeScope() {
        return this.getBaseService().getNoticeToTypeSelect();
    }

    @ApiOperation("获取通知是否获取列表")
    @GetMapping("/noticeOverdue")
    @PreAuthorize("@el.check('Notice:use')")
    @RateLimiter(limitType = LimitType.IP)
    public List<EnumSelectVO> getNoticeOverdue() {
        return this.getBaseService().getOverdueSelect();
    }

    @ApiOperation("获取发送人列表")
    @GetMapping("/distinctUser")
    @PreAuthorize("@el.check('Notice:use')")
    @RateLimiter(limitType = LimitType.IP)
    public List<VueSelectVO> getDistinctUser() {
        return this.getBaseService().getDistinctUserSelect();
    }
}
