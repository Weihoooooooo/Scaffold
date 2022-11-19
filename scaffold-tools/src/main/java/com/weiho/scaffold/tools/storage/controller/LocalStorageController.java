package com.weiho.scaffold.tools.storage.controller;

import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.mp.controller.CommonController;
import com.weiho.scaffold.tools.storage.entity.LocalStorage;
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
import java.util.Map;
import java.util.Set;

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
public class LocalStorageController extends CommonController<LocalStorageService, LocalStorage> {
    @ApiOperation("查询文件")
    @PreAuthorize("@el.check('Storage:list')")
    @GetMapping
    public Map<String, Object> getLocalStorage(@Validated LocalStorageQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().findAll(criteria, pageable);
    }

    @Logging(title = "导出文件数据")
    @ApiOperation("导出文件数据")
    @GetMapping("/download")
    @PreAuthorize("@el.check('Storage:list')")
    public void download(HttpServletResponse response, @Validated LocalStorageQueryCriteria criteria) throws IOException {
        this.getBaseService().download(this.getBaseService().findAll(criteria), response);
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
        return Result.success(this.getBaseService().upload(file, filename));
    }

    @Logging(title = "下载文件")
    @ApiOperation("下载文件")
    @PreAuthorize("@el.check('Storage:download')")
    @GetMapping("/download/{id}")
    public void download(HttpServletResponse response, @PathVariable String id) throws IOException {
        this.getBaseService().download(IdSecureUtils.des().decrypt(id), response);
    }


    @Logging(title = "修改文件名", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @ApiOperation("修改文件名")
    @PreAuthorize("@el.check('Storage:update')")
    public Result updateLocalStorage(@Validated @RequestBody LocalStorageVO localStorageVO) {
        return resultMessage(Operate.UPDATE, this.getBaseService().updateFileName(localStorageVO));
    }

    @Logging(title = "删除文件", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除文件")
    @PreAuthorize("@el.check('Storage:delete')")
    @DeleteMapping
    public Result deleteLocalStorage(@RequestBody Set<String> ids) {
        return resultMessage(Operate.DELETE, this.getBaseService().deleteByIds(filterCollNullAndDecrypt(ids)));
    }
}
