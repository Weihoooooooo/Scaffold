package com.weiho.scaffold.tools.storage.controller;

import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.tools.storage.entity.criteria.LocalStorageQueryCriteria;
import com.weiho.scaffold.tools.storage.entity.vo.LocalStorageVO;
import com.weiho.scaffold.tools.storage.service.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-10-27
 */
@Api(tags = "本地存储")
@RestController
@RequestMapping("/api/v1/localStorage")
@RequiredArgsConstructor
public class LocalStorageController {
    private final LocalStorageService localStorageService;

    @ApiOperation("查询文件")
    @PreAuthorize("@el.check('Storage:list')")
    @GetMapping
    public Map<String, Object> getLocalStorage(LocalStorageQueryCriteria criteria, Pageable pageable) {
        return localStorageService.findAll(criteria, pageable);
    }

    @Logging(title = "导出文件数据")
    @ApiOperation("导出文件数据")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Storage:list')")
    public void download(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(localStorageService.findAll(criteria), response);
    }

    @Logging(title = "上传文件", businessType = BusinessTypeEnum.INSERT)
    @PreAuthorize("@el.check('Storage:add')")
    @PostMapping
    @ApiOperation("上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "formData", name = "file", value = "文件", dataType = "file", dataTypeClass = File.class),
            @ApiImplicitParam(name = "filename", value = "文件名", dataType = "String", dataTypeClass = String.class)
    })
    public Result uploadLocalStorage(@RequestPart("file") MultipartFile file,
                                     @RequestParam(value = "filename", required = false) String filename) {
        return Result.success(localStorageService.upload(file, filename));
    }

    @Logging(title = "下载文件")
    @ApiOperation("下载文件")
    @PreAuthorize("@el.check('Storage:download')")
    @GetMapping("/download/{id}")
    public void download(HttpServletResponse response, @PathVariable Serializable id) throws IOException {
        localStorageService.download(id, response);
    }


    @Logging(title = "修改文件名", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改文件名")
    @PreAuthorize("@el.check('Storage:update')")
    public Result updateLocalStorage(@Validated @RequestBody LocalStorageVO localStorageVO) {
        if (localStorageService.updateFileName(localStorageVO)) {
            return Result.success(I18nMessagesUtils.get("update.success.tip"));
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("update.fail.tip"));
        }
    }

    @Logging(title = "删除文件", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除文件")
    @PreAuthorize("@el.check('Storage:delete')")
    @DeleteMapping
    public Result deleteLocalStorage(@RequestBody Long[] ids) {
        if (ids != null && ids.length > 0) {
            if (localStorageService.deleteByIds(ids)) {
                return Result.success(I18nMessagesUtils.get("delete.success.tip"));
            } else {
                return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("delete.fail.tip"));
            }
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("delete.fail.tip"));
        }
    }
}
