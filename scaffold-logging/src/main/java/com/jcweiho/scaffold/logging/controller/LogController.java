package com.jcweiho.scaffold.logging.controller;

import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.entity.criteria.LogQueryCriteria;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.logging.enums.LogTypeEnum;
import com.jcweiho.scaffold.logging.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-08-06
 */
@Api(tags = "操作日志管理")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;

    @ApiOperation("查询操作日志")
    @GetMapping("/logs")
    @PreAuthorize("@el.check('PlayLog:list')")
    public Map<String, Object> getAll(@Validated LogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType(LogTypeEnum.INFO.getMsg());
        return logService.findAllByPage(criteria, pageable);
    }

    @ApiOperation("查询异常日志")
    @GetMapping("/errorLogs")
    @PreAuthorize("@el.check('ErrorLog:list')")
    public Map<String, Object> getAllError(@Validated LogQueryCriteria criteria, Pageable pageable) {
        criteria.setLogType(LogTypeEnum.ERROR.getMsg());
        return logService.findAllByPage(criteria, pageable);
    }

    @Logging(title = "导出操作日志")
    @ApiOperation("导出操作日志")
    @GetMapping("/logs/download")
    @PreAuthorize("@el.check('PlayLog:list')")
    public void downloadInfo(@Validated LogQueryCriteria criteria, HttpServletResponse response) throws IOException {
        criteria.setLogType(LogTypeEnum.INFO.getMsg());
        logService.download(logService.findAll(criteria), response);
    }

    @Logging(title = "导出异常日志")
    @ApiOperation("导出异常日志")
    @GetMapping("/errorLogs/download")
    @PreAuthorize("@el.check('ErrorLog:list')")
    public void downloadError(@Validated LogQueryCriteria criteria, HttpServletResponse response) throws IOException {
        criteria.setLogType(LogTypeEnum.ERROR.getMsg());
        logService.download(logService.findAll(criteria), response);
    }

    @GetMapping("/errorLogs/{id}")
    @ApiOperation("查询异常日志详情")
    @PreAuthorize("@el.check('ErrorLog:detail')")
    @ApiImplicitParam(name = "id", value = "日志的主键", required = true, dataType = "Long", dataTypeClass = Long.class)
    public Object findByErrorDetail(@PathVariable("id") Long id) {
        return logService.findByErrorDetail(id);
    }

    @Logging(title = "删除所有INFO日志", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除所有INFO日志")
    @DeleteMapping("/logs")
    @PreAuthorize("@el.check('PlayLog:delete')")
    public void deleteAllInfo() {
        logService.deleteAllByInfo();
    }

    @Logging(title = "删除所有ERROR日志", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除所有ERROR日志")
    @DeleteMapping("/errorLogs")
    @PreAuthorize("@el.check('ErrorLog:delete')")
    public void deleteAllError() {
        logService.deleteAllByError();
    }

    @ApiOperation("查询个人操作日志")
    @GetMapping("/center/logs")
    public Map<String, Object> getMyLogs(Pageable pageable) {
        return logService.findByUsername(LogTypeEnum.INFO, pageable);
    }

    @ApiOperation("查询个人异常日志")
    @GetMapping("/center/errorLogs")
    public Map<String, Object> getMyErrorLogs(Pageable pageable) {
        return logService.findByUsername(LogTypeEnum.ERROR, pageable);
    }
}
