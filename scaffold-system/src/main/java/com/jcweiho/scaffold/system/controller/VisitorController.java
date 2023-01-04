package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Visitor;
import com.jcweiho.scaffold.system.entity.criteria.VisitorQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.VisitorVO;
import com.jcweiho.scaffold.system.service.VisitorService;
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
 * 访客信息 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2023-01-02
 */
@Api(tags = "访客信息管理")
@RestController
@RequestMapping("/api/v1/visitors")
@RequiredArgsConstructor
public class VisitorController extends CommonController<VisitorService, Visitor> {

    @ApiOperation("查询访客信息列表")
    @PreAuthorize("@el.check('Visitor:list')")
    @GetMapping
    public Map<String, Object> getVisitorList(@Validated VisitorQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging("导出访客信息")
    @ApiOperation("导出访客信息")
    @PreAuthorize("@el.check('Visitor:list')")
    @GetMapping("/download")
    public void download(@Validated VisitorQueryCriteria criteria, HttpServletResponse response) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().findAll(criteria)), response);
    }

    @Logging(title = "新增访客信息", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("新增访客信息")
    @PreAuthorize("@el.check('Visitor:add')")
    @PostMapping
    public Result addVisitor(@RequestBody @Validated VisitorVO resources) {
        return resultMessage(Operate.ADD, this.getBaseService().addVisitor(resources));
    }

    @Logging(title = "修改访客信息", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("修改访客信息")
    @PreAuthorize("@el.check('Visitor:update')")
    @PutMapping
    public Result updateVisitor(@RequestBody @Validated VisitorVO resources) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateVisitor(resources));
    }

    @Logging(title = "删除访客信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除访客信息")
    @PreAuthorize("@el.check('Visitor:delete')")
    @DeleteMapping
    public Result deleteVisitor(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteVisitor(filterCollNullAndDecrypt(ids)));
    }
}
