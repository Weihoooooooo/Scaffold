package com.jcweiho.scaffold.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.result.Result;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.controller.CommonController;
import com.jcweiho.scaffold.redis.limiter.annotation.RateLimiter;
import com.jcweiho.scaffold.redis.limiter.enums.LimitType;
import com.jcweiho.scaffold.redis.util.RedisUtils;
import com.jcweiho.scaffold.system.cache.service.CacheRefresh;
import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.User;
import com.jcweiho.scaffold.system.entity.criteria.UserQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.UserPassVO;
import com.jcweiho.scaffold.system.entity.vo.UserVO;
import com.jcweiho.scaffold.system.entity.vo.VerificationVO;
import com.jcweiho.scaffold.system.mapper.RoleMapper;
import com.jcweiho.scaffold.system.security.service.LoginService;
import com.jcweiho.scaffold.system.security.token.utils.TokenUtils;
import com.jcweiho.scaffold.system.service.RoleService;
import com.jcweiho.scaffold.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "??????????????????")
@RequiredArgsConstructor
public class UserController extends CommonController<UserService, User> {
    private final UserDetailsService userDetailsService;
    private final ScaffoldSystemProperties properties;
    private final PasswordEncoder passwordEncoder;
    private final CacheRefresh cacheRefresh;
    private final RoleService roleService;
    private final LoginService loginService;
    private final RoleMapper roleMapper;
    private final RedisUtils redisUtils;
    private final TokenUtils tokenUtils;

    @ApiOperation("??????????????????????????????")
    @GetMapping("/info")
    @RateLimiter(limitType = LimitType.IP)
    public Map<String, Object> getUserInfo() {
        List<Role> roles = roleService.findListByUser(this.getBaseService().findByUsername(SecurityUtils.getUsername()));
        return new LinkedHashMap<String, Object>(2) {{
            put("userInfo", userDetailsService.loadUserByUsername(SecurityUtils.getUsername()));
            put("maxLevel", CollUtils.min(roles.stream().map(Role::getLevel).collect(Collectors.toList())));
        }};
    }

    @ApiOperation("??????????????????")
    @PreAuthorize("@el.check('User:list')")
    @GetMapping
    public Map<String, Object> getUserList(@Validated UserQueryCriteria criteria, Pageable pageable) {
        return this.getBaseService().getUserList(criteria, pageable);
    }

    @ApiOperation("????????????")
    @PostMapping("/pass")
    @Logging(title = "????????????", saveRequestData = false)
    public Result updatePass(@Validated @RequestBody UserPassVO passVO) throws Exception {
        // ????????????
        String oldPass = RsaUtils.decryptByPrivateKey(properties.getRsaProperties().getPrivateKey(), passVO.getOldPassword());
        String newPass = RsaUtils.decryptByPrivateKey(properties.getRsaProperties().getPrivateKey(), passVO.getNewPassword());
        User user = this.getBaseService().findByUsername(SecurityUtils.getUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException(I18nMessagesUtils.get("user.update.oldPassError"));
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException(I18nMessagesUtils.get("user.update.tip"));
        }
        this.getBaseService().updatePass(user.getUsername(), passwordEncoder.encode(newPass));
        user.setPassword(passwordEncoder.encode(newPass));
        cacheRefresh.updateUserCache(user);
        return Result.success(I18nMessagesUtils.get("update.success.tip"));
    }

    @Logging(title = "????????????", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("????????????")
    @PostMapping("/email")
    public Result updateEmail(@Validated @RequestBody VerificationVO verificationVO) throws Exception {
        String newEmail = verificationVO.getNewEmail() + verificationVO.getSuffix().getDisplay();
        // ??????????????????????????????????????????
        List<User> usersForNewEmail = this.getBaseService().list(new LambdaQueryWrapper<User>().eq(User::getEmail, AesUtils.encrypt(newEmail)));
        // ??????????????????
        if (usersForNewEmail.size() != 0) {
            throw new BadRequestException(I18nMessagesUtils.get("mail.change.error"));
        } else {
            // ???????????????
            // ???????????????
            String code = redisUtils.getString(verificationVO.getUuid());
            redisUtils.del(verificationVO.getUuid());
            if (StringUtils.isBlank(code)) {
                throw new BadRequestException(I18nMessagesUtils.get("captcha.exception.not.found"));
            }
            if (StringUtils.isBlank(verificationVO.getCode()) || !verificationVO.getCode().equals(code)) {
                throw new BadRequestException(I18nMessagesUtils.get("captcha.exception.error"));
            }
            // ????????????
            String password = RsaUtils.decryptByPrivateKey(properties.getRsaProperties().getPrivateKey(), verificationVO.getPassword());
            User user = this.getBaseService().findByUsername(SecurityUtils.getUsername());
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadRequestException(I18nMessagesUtils.get("mail.change.pass.error"));
            }
            this.getBaseService().updateEmail(user.getUsername(), newEmail);
            // ????????????
            user.setEmail(newEmail);
            cacheRefresh.updateUserCache(user);
            tokenUtils.putUserDetails(userDetailsService.loadUserByUsername(SecurityUtils.getUsername()));
            return Result.success(I18nMessagesUtils.get("update.success.tip"));
        }
    }

    @Logging(title = "????????????", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("????????????")
    @PostMapping("/avatar")
    public Result updateAvatar(@RequestParam MultipartFile file) {
        this.getBaseService().updateAvatar(file);
        // ????????????
        tokenUtils.putUserDetails(userDetailsService.loadUserByUsername(SecurityUtils.getUsername()));
        return Result.success(I18nMessagesUtils.get("update.success.tip"));
    }

    @Logging(title = "????????????", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation("????????????")
    @PutMapping
    @PreAuthorize("@el.check('User:update')")
    public Result updateUser(@Validated @RequestBody UserVO resources) {
        roleService.checkLevel(resources.getId());
        return resultMessage(Operate.UPDATE, this.getBaseService().updateUser(resources));
    }

    @Logging(title = "????????????", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation("????????????")
    @PostMapping
    @PreAuthorize("@el.check('User:add')")
    public Result createUser(@Validated @RequestBody UserVO resources) {
        roleService.checkLevel(resources.getRoles());
        return resultMessage(Operate.ADD, this.getBaseService().createUser(resources));
    }

    @Logging(title = "????????????", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation("????????????")
    @DeleteMapping
    @PreAuthorize("@el.check('User:delete')")
    public Result deleteUser(@RequestBody Set<String> idStrings) {
        Set<Long> ids = filterCollNullAndDecrypt(idStrings);
        for (Long id : ids) {
            // ???????????????????????????
            Integer currentLevel = CollUtils.min(roleMapper.findListByUserId(SecurityUtils.getUserId()).stream().map(Role::getLevel).collect(Collectors.toList()));
            Integer optLevel;
            List<Role> roles = roleMapper.findListByUserId(id);
            if (roles != null && roles.size() > 0) {
                optLevel = CollUtils.min(roles.stream().map(Role::getLevel).collect(Collectors.toList()));
            } else {
                optLevel = 999;
            }
            if (currentLevel > optLevel) {
                throw new BadRequestException(I18nMessagesUtils.get("delete.error.tip") + ":[" + this.getBaseService().getById(id).getUsername() + "]");
            }
        }
        return resultMessage(Operate.DELETE, this.getBaseService().deleteUser(ids));
    }

    @Logging("??????????????????")
    @ApiOperation("??????????????????")
    @GetMapping("/download")
    @PreAuthorize("@el.check('User:list')")
    public void download(HttpServletResponse response, @Validated UserQueryCriteria criteria) throws IOException {
        this.getBaseService().download(this.getBaseService().convertToVO(this.getBaseService().getAll(criteria)), response);
    }

    @ApiOperation("?????????????????????????????????")
    @PostMapping("/verifyAccount")
    public Result verifyAccount(@RequestBody Map<String, Object> map) throws Exception {
        filterMapNull(map, I18nMessagesUtils.get("param.error"));
        return loginService.verifyAccount(MapUtils.getStr(map, "password"));
    }

}
