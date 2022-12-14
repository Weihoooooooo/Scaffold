package com.jcweiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.redis.util.RedisUtils;
import com.jcweiho.scaffold.system.cache.service.CacheRefresh;
import com.jcweiho.scaffold.system.entity.Avatar;
import com.jcweiho.scaffold.system.entity.Role;
import com.jcweiho.scaffold.system.entity.User;
import com.jcweiho.scaffold.system.entity.UsersRoles;
import com.jcweiho.scaffold.system.entity.convert.JwtUserVOConvert;
import com.jcweiho.scaffold.system.entity.convert.UserVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.UserQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.AuditEnum;
import com.jcweiho.scaffold.system.entity.vo.UserVO;
import com.jcweiho.scaffold.system.mapper.AvatarMapper;
import com.jcweiho.scaffold.system.mapper.RoleMapper;
import com.jcweiho.scaffold.system.mapper.UserMapper;
import com.jcweiho.scaffold.system.security.vo.JwtUserVO;
import com.jcweiho.scaffold.system.service.AvatarService;
import com.jcweiho.scaffold.system.service.SysSettingService;
import com.jcweiho.scaffold.system.service.UserService;
import com.jcweiho.scaffold.system.service.UsersRolesService;
import com.jcweiho.scaffold.tools.mail.util.MailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends CommonServiceImpl<UserMapper, User> implements UserService {
    private final AvatarService avatarService;
    private final AvatarMapper avatarMapper;
    private final RoleMapper roleMapper;
    private final JwtUserVOConvert jwtUserVOConvert;
    private final ScaffoldSystemProperties properties;
    private final UserVOConvert userVOConvert;
    private final UsersRolesService usersRolesService;
    private final RedisUtils redisUtils;
    private final SysSettingService sysSettingService;
    private final PasswordEncoder passwordEncoder;
    private final CacheRefresh cacheRefresh;

    @Override
    public JwtUserVO getBaseJwtUserVO(User user) {
        //????????????
        JwtUserVO jwtUserVO = jwtUserVOConvert.toPojo(user);
        //??????????????????
        jwtUserVO.setAvatar(avatarService.selectByAvatarId(user.getAvatarId(), user.getUsername()));
        return jwtUserVO;
    }

    @Override
    @Cacheable(value = "Scaffold:Commons:User", key = "'loadUserByUsername:' + #p0", unless = "#result == null || #result.enabled == false")
    public User findByUsername(String username) {
        try {
            return this.lambdaQuery().eq(User::getUsername, username).eq(User::getIsDel, 0).one();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String encryptPassword) {
        this.lambdaUpdate().set(User::getPassword, encryptPassword, "typeHandler=com.jcweiho.scaffold.mp.handler.EncryptHandler")
                .set(User::getLastPassResetTime, DateUtils.getNowDateFormat(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS))
                .eq(User::getUsername, username)
                .eq(User::getIsDel, 0).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String newEmail) {
        this.lambdaUpdate().set(User::getEmail, newEmail, "typeHandler=com.jcweiho.scaffold.mp.handler.EncryptHandler")
                .eq(User::getUsername, username).eq(User::getIsDel, 0).update();
    }

    @Override
    public void updateAvatar(MultipartFile multipartFile) {
        User user = findByUsername(SecurityUtils.getUsername());
        // ??????????????????????????????
        Avatar avatar = avatarService.selectByAvatarId(user.getAvatarId(), user.getUsername());
        if (ObjectUtil.isNull(avatar)) {
            // ??????????????????????????? ??????
            avatar = new Avatar();
            getAvatar(multipartFile, avatar);
            // ??????????????????
            boolean result = avatarService.save(avatar);
            if (result) {
                // ???????????????,???????????????????????????ID
                Long avatarNewId = avatarService.getOne(new LambdaQueryWrapper<Avatar>().eq(Avatar::getRealName, avatar.getRealName())).getId();
                // ??????????????????
                user.setAvatarId(avatarNewId);
                this.updateById(user);
                // ??????????????????
                cacheRefresh.updateUserCache(user);
            }
        } else {
            // ???????????????????????? ??????
            String oldFileName = avatar.getRealName();
            String oldFileNamePath = properties.getResourcesProperties().getAvatarLocalAddressPrefix() + oldFileName;
            getAvatar(multipartFile, avatar);
            // ????????????
            avatarService.updateAvatar(avatar, user.getUsername());
            if (StringUtils.isNotBlank(oldFileNamePath)) {
                FileUtils.del(oldFileNamePath);
            }
        }
    }

    /**
     * ???????????????????????????
     *
     * @param multipartFile ??????1
     * @param avatar        ??????2
     */
    private void getAvatar(MultipartFile multipartFile, Avatar avatar) {
        File file = FileUtils.upload(multipartFile, properties.getResourcesProperties().getAvatarLocalAddressPrefix());
        avatar.setRealName(file.getName());
        avatar.setPath(properties.getResourcesProperties().getAvatarServerAddressPrefix() + file.getName());
        avatar.setSize(FileUtils.getSize(multipartFile.getSize()));
        if (SecurityUtils.getUsername().equals("root")) {
            avatar.setEnabled(AuditEnum.AUDIT_OK);
        } else {
            avatar.setEnabled(AuditEnum.AUDIT_NO);
        }
    }

    @Override
    public Map<String, Object> getUserList(UserQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<User> pageInfo = new PageInfo<>(this.getAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public List<UserVO> convertToVO(List<User> users) {
        List<UserVO> userVOS = ListUtils.list(false);
        for (User user : users) {
            UserVO userVO = userVOConvert.toPojo(user);
            userVO.setAvatar(avatarMapper.selectById(user.getAvatarId()));
            userVO.setRoles(roleMapper.findSetByUserId(user.getId()));
            userVOS.add(userVO);
        }
        return userVOS;
    }

    @Override
    public List<User> getAll(UserQueryCriteria criteria) {
        criteria.setPhone(LikeCipherUtils.likeEncrypt(criteria.getPhone()));
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(User.class, criteria)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserVO resource) {
        IdSecureUtils.verifyIdNotNull(resource.getId());
        // ??????ID????????????
        User user = this.getById(resource.getId());
        // ?????????????????????
        String oldUsername = user.getUsername();
        // ??????????????????????????????????????????????????????????????????
        if (!oldUsername.equals(resource.getUsername())) {
            throw new BadRequestException(I18nMessagesUtils.get("update.username.tip"));
        }
        MailUtils.checkEmail(resource.getEmail());
        IdSecureUtils.verifyIdNotNull(resource.getId());
        // ???????????????????????????????????????
        User userUsername = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, resource.getUsername()));
        // ????????????????????????????????????
        User userEmail = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, AesUtils.encrypt(resource.getEmail())));

        // ???????????????????????????
        if (ObjectUtil.isNotNull(userUsername) && !user.getId().equals(userUsername.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("username.exists"));
        }

        if (ObjectUtil.isNotNull(userEmail) && !user.getEmail().equals(userEmail.getEmail())) {
            throw new BadRequestException(I18nMessagesUtils.get("email.exists"));
        }

        user.setUsername(resource.getUsername());
        user.setEmail(resource.getEmail());
        user.setSex(resource.getSex());
        user.setPhone(resource.getPhone());
        user.setEnabled(resource.isEnabled());
        user.setLastPassResetTime(resource.getLastPassResetTime());

        // ????????????????????????????????????
        boolean result = this.saveOrUpdate(user);

        if (result) {
            // ?????????????????????ID??????
            Set<Long> oldRoleIds = resource.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
            // ??????????????????ID??????
            Set<Long> newRoleIds = roleMapper.findSetByUserId(resource.getId()).stream().map(Role::getId).collect(Collectors.toSet());

            // ??????????????????????????????????????????
            if (CollUtils.isCollectionNotEqual(oldRoleIds, newRoleIds) && CollUtils.isNotBlank(resource.getRoles())) {
                // ??????????????????
                usersRolesService.removeById(resource.getId());
                // ??????????????????
                boolean flag = usersRolesService.saveBatch(
                        resource.getRoles().stream().map(i -> {
                            UsersRoles usersRoles = new UsersRoles();
                            usersRoles.setUserId(resource.getId());
                            usersRoles.setRoleId(i.getId());
                            return usersRoles;
                        }).collect(Collectors.toList())
                );

                if (flag) {
                    // ??????????????????????????????????????????????????????
                    String keyPermissions = redisUtils.getRedisCommonsKey("Permission", oldUsername);
                    String keyRoles = redisUtils.getRedisCommonsKey("Roles", oldUsername);
                    if (redisUtils.hasKey(keyPermissions) && redisUtils.hasKey(keyRoles)) {
                        // ??????????????????
                        redisUtils.del(keyPermissions, keyRoles);
                        // ??????????????????????????????
                        cacheRefresh.updateRolesCacheForGrantedAuthorities(user.getId(), user.getUsername());
                        cacheRefresh.updateRolesCacheForRoleList(user);
                    }
                }
                return flag;
            }
        }
        return result;
    }

    @Override
    public void download(List<UserVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (UserVO userVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.user.username"), userVO.getUsername());
            map.put(I18nMessagesUtils.get("download.avatar.path"), userVO.getAvatar().getPath());
            map.put(I18nMessagesUtils.get("download.avatar.size"), userVO.getAvatar().getSize());
            map.put(I18nMessagesUtils.get("download.sex"), userVO.getSex().getDisplay());
            map.put(I18nMessagesUtils.get("download.email"), userVO.getEmail());
            map.put(I18nMessagesUtils.get("download.phone"), userVO.getPhone());
            map.put(I18nMessagesUtils.get("download.user.state"), userVO.isEnabled() ? I18nMessagesUtils.get("download.yes") : I18nMessagesUtils.get("download.no"));
            map.put(I18nMessagesUtils.get("download.user.lastPass"), userVO.getLastPassResetTime());
            map.put(I18nMessagesUtils.get("download.user.add.time"), userVO.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(UserVO resource) {
        IdSecureUtils.verifyIdNull(resource.getId());
        MailUtils.checkEmail(resource.getEmail());
        // ??????????????????????????????????????????
        User userUsername = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, resource.getUsername()));
        if (ObjectUtil.isNotNull(userUsername)) {
            throw new BadRequestException(I18nMessagesUtils.get("username.exists"));
        }
        // ?????????????????????
        User userEmail = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, AesUtils.encrypt(resource.getEmail())));
        if (ObjectUtil.isNotNull(userEmail)) {
            throw new BadRequestException(I18nMessagesUtils.get("email.exists"));
        }
        // ????????????
        User user = userVOConvert.toEntity(resource);
        // ??????????????????
        user.setPassword(passwordEncoder.encode(sysSettingService.getSysSettings().getUserInitPassword()));
        // ??????
        boolean result = this.save(user);

        // ?????????????????????????????????????????????
        if (result && CollUtils.isNotEmpty(resource.getRoles())) {
            Long userId = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername())).getId();
            return usersRolesService.saveBatch(
                    resource.getRoles().stream().map(i -> {
                        UsersRoles usersRoles = new UsersRoles();
                        usersRoles.setUserId(userId);
                        usersRoles.setRoleId(i.getId());
                        return usersRoles;
                    }).collect(Collectors.toList())
            );
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Set<Long> ids) {
        for (Long id : ids) {
            usersRolesService.removeById(id);
        }
        return this.removeByIds(ids);
    }
}
