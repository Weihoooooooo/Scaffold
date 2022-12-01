package com.jcweiho.scaffold.system.controller;

import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.system.entity.Avatar;
import com.jcweiho.scaffold.system.entity.criteria.AvatarQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.AvatarEnabledVO;
import com.jcweiho.scaffold.system.service.AvatarService;
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
 * 用户头像表 前端控制器
 * </p>
 *
 * @author Weiho
 * @since 2022-07-29
 */
@Api(tags = "头像管理")
@RestController
@RequestMapping("/api/v1/avatars")
@RequiredArgsConstructor
public class AvatarController extends CommonController<AvatarService, Avatar> {
    @ApiOperation("查询头像列表")
    @GetMapping
    @PreAuthorize("@el.check('Avatar:list')")
    public Map<String, Object> getAvatarList(@Validated AvatarQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().selectAvatarList(criteria, pageable);
    }

    @Logging("导出头像信息")
    @GetMapping("/download")
    @ApiOperation("导出头像信息")
    @PreAuthorize("@el.check('Avatar:list')")
    public void download(HttpServletResponse response, @Validated AvatarQueryCriteria criteria) throws IOException {
        this.getBaseService().download(this.getBaseService().getAll(criteria), response);
    }

    @DeleteMapping
    @Logging(title = "删除头像信息", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("删除头像信息")
    @PreAuthorize("@el.check('Avatar:delete')")
    public Result deleteAvatar(@RequestBody Set<String> idStrings) {
        Set<Long> ids = filterCollNullAndDecrypt(idStrings, I18nMessagesUtils.get("avatar.error.tip"));
        return resultMessage(Operate.DELETE, this.getBaseService().deleteAvatar(ids));
    }

    @PutMapping
    @Logging("审核用户头像")
    @ApiOperation("审核用户头像")
    @PreAuthorize("@el.check('Avatar:update')")
    public Result updateEnabled(@RequestBody AvatarEnabledVO avatarEnabledVO) {
        return resultMessage(Operate.OPERATE, this.getBaseService().updateEnabled(avatarEnabledVO));
    }
}
