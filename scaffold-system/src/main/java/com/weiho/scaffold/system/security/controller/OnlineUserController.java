package com.weiho.scaffold.system.security.controller;

import com.alibaba.fastjson2.JSON;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.util.DesUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.logging.annotation.Logging;
import com.weiho.scaffold.logging.enums.BusinessTypeEnum;
import com.weiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.weiho.scaffold.redis.limiter.enums.LimitType;
import com.weiho.scaffold.redis.util.RedisUtils;
import com.weiho.scaffold.system.security.service.OnlineUserService;
import com.weiho.scaffold.system.security.vo.online.OnlineUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/8/29
 */
@Api(tags = "在线用户管理")
@RestController
@RequestMapping("/api/v1/online")
@RequiredArgsConstructor
public class OnlineUserController {
    private final OnlineUserService onlineUserService;
    private final RedisUtils redisUtils;
    private final ScaffoldSystemProperties properties;

    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check('Online:list')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filter", value = "模糊查询字段,不填则全查", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "缓存的key匹配方式,默认值为0,默认值匹配度会更高", dataType = "Integer", dataTypeClass = Integer.class)
    })
    public Map<String, Object> getAll(@RequestParam(value = "filter", defaultValue = "") String filter,
                                      @RequestParam(value = "type", defaultValue = "0") int type,
                                      Pageable pageable) {
        return onlineUserService.getAll(filter, type, pageable);
    }

    @Logging(title = "导出在线用户数据")
    @ApiOperation("导出在线用户数据")
    @PreAuthorize("@el.check('Online:list')")
    @GetMapping("/download")
    @RateLimiter(limitType = LimitType.IP)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filter", value = "模糊查询字段,不填则全查", dataType = "String", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "缓存的key匹配方式,默认值为0,默认值匹配度会更高", dataType = "Integer", dataTypeClass = Integer.class)
    })
    public void download(HttpServletResponse response,
                         @RequestParam(value = "filter", defaultValue = "") String filter,
                         @RequestParam(value = "type", defaultValue = "0") int type) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter, type), response);
    }

    @Logging(title = "踢在线用户下线", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("踢在线用户下线")
    @PreAuthorize("@el.check('Online:list','Online:delete')")
    @DeleteMapping
    public Result delete(@RequestBody Set<String> keys) throws Exception {
        Set<String> usernames = new HashSet<>();
        for (String key : keys) {
            OnlineUserVO onlineUserVO = JSON.parseObject(redisUtils.getString(properties.getJwtProperties().getOnlineKey() + DesUtils.desDecrypt(key)), OnlineUserVO.class);
            usernames.add(onlineUserVO.getUsername());
            onlineUserService.kickOut(key);
        }
        return Result.success(I18nMessagesUtils.get("online.user") + usernames + I18nMessagesUtils.get("online.kick.tip"));
    }
}
