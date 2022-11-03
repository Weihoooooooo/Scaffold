package com.weiho.scaffold.system.controller;

import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.ResultUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
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
public class NoticeController {
    private final NoticeService noticeService;

    @ApiOperation("查询通知列表")
    @GetMapping
    @PreAuthorize("@el.check('Notice:use')")
    public Map<String, Object> getNoticeList(NoticeQueryCriteria criteria, Pageable pageable) {
        return noticeService.getNoticeList(criteria, pageable);
    }

    @Logging(title = "导出通知信息")
    @ApiOperation("导出通知信息")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Notice:use')")
    public void download(HttpServletResponse response, NoticeQueryCriteria criteria) throws IOException {
        noticeService.download(response, noticeService.findAll(criteria));
    }

    @Logging(title = "添加通知信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("添加通知信息")
    @PostMapping
    @PreAuthorize("@el.check('Notice:add')")
    public Result addNotice(@Validated @RequestBody NoticeVO resources) {
        return ResultUtils.addMessage(noticeService.addNotice(resources));
    }

    @Logging(title = "修改通知信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改通知信息")
    @PutMapping
    @PreAuthorize("@el.check('Notice:update')")
    public Result updateNotice(@RequestBody NoticeVO resources) {
        return ResultUtils.updateMessage(noticeService.updateNotice(resources));
    }

    @Logging(title = "删除通知信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除通知信息")
    @DeleteMapping
    @PreAuthorize("@el.check('Notice:delete')")
    public Result deleteNotice(@RequestBody Set<Long> ids) {
        return ResultUtils.deleteMessage(ids, noticeService.delete(ids));
    }
}
