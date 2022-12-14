package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.system.entity.Notice;
import com.jcweiho.scaffold.system.entity.criteria.NoticeQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.NoticeVO;
import com.jcweiho.scaffold.system.service.NoticeService;
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

    @Logging("导出通知信息")
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

    @ApiOperation("获取发送人列表")
    @GetMapping("/distinctUser")
    @PreAuthorize("@el.check('Notice:use')")
    @RateLimiter(limitType = LimitType.IP)
    public List<VueSelectVO> getDistinctUser() {
        return this.getBaseService().getDistinctUserSelect();
    }
}
