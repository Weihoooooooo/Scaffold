package com.weiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.*;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.redis.util.RedisUtils;
import com.weiho.scaffold.system.cache.service.CacheRefresh;
import com.weiho.scaffold.system.entity.Avatar;
import com.weiho.scaffold.system.entity.Role;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.entity.UsersRoles;
import com.weiho.scaffold.system.entity.convert.JwtUserVOConvert;
import com.weiho.scaffold.system.entity.convert.UserVOConvert;
import com.weiho.scaffold.system.entity.criteria.UserQueryCriteria;
import com.weiho.scaffold.system.entity.enums.AuditEnum;
import com.weiho.scaffold.system.entity.vo.UserVO;
import com.weiho.scaffold.system.mapper.AvatarMapper;
import com.weiho.scaffold.system.mapper.RoleMapper;
import com.weiho.scaffold.system.mapper.UserMapper;
import com.weiho.scaffold.system.security.vo.JwtUserVO;
import com.weiho.scaffold.system.service.AvatarService;
import com.weiho.scaffold.system.service.SysSettingService;
import com.weiho.scaffold.system.service.UserService;
import com.weiho.scaffold.system.service.UsersRolesService;
import com.weiho.scaffold.tools.mail.util.MailUtils;
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
 * 系统用户表 服务实现类
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
        //转化对象
        JwtUserVO jwtUserVO = jwtUserVOConvert.toPojo(user);
        //放入头像对象
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
        this.lambdaUpdate().set(User::getPassword, encryptPassword, "typeHandler=com.weiho.scaffold.mp.handler.EncryptHandler")
                .set(User::getLastPassResetTime, DateUtils.getNowDateFormat(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS))
                .eq(User::getUsername, username)
                .eq(User::getIsDel, 0).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String newEmail) {
        this.lambdaUpdate().set(User::getEmail, newEmail, "typeHandler=com.weiho.scaffold.mp.handler.EncryptHandler")
                .eq(User::getUsername, username).eq(User::getIsDel, 0).update();
    }

    @Override
    public void updateAvatar(MultipartFile multipartFile) {
        User user = findByUsername(SecurityUtils.getUsername());
        // 查找该用户的头像信息
        Avatar avatar = avatarService.selectByAvatarId(user.getAvatarId(), user.getUsername());
        if (ObjectUtil.isNull(avatar)) {
            // 若该用户不存在头像 插入
            avatar = new Avatar();
            getAvatar(multipartFile, avatar);
            // 保存头像信息
            boolean result = avatarService.save(avatar);
            if (result) {
                // 插入成功后,查询刚刚插入的头像ID
                Long avatarNewId = avatarService.getOne(new LambdaQueryWrapper<Avatar>().eq(Avatar::getRealName, avatar.getRealName())).getId();
                // 更新用户信息
                user.setAvatarId(avatarNewId);
                this.updateById(user);
                // 删除用户缓存
                cacheRefresh.updateUserCache(user);
            }
        } else {
            // 若该用户存在头像 修改
            String oldFileName = avatar.getRealName();
            String oldFileNamePath = properties.getResourcesProperties().getAvatarLocalAddressPrefix() + oldFileName;
            getAvatar(multipartFile, avatar);
            // 更新缓存
            avatarService.updateAvatar(avatar, user.getUsername());
            if (StringUtils.isNotBlank(oldFileNamePath)) {
                FileUtils.del(oldFileNamePath);
            }
        }
    }

    /**
     * 提取上述方法公共体
     *
     * @param multipartFile 参数1
     * @param avatar        参数2
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
        // 根据ID查找用户
        User user = this.getById(resource.getId());
        // 保存旧的用户名
        String oldUsername = user.getUsername();
        // 为了考虑在线用户的缓存，暂时还不能修改用户名
        if (!oldUsername.equals(resource.getUsername())) {
            throw new BadRequestException(I18nMessagesUtils.get("update.username.tip"));
        }
        MailUtils.checkEmail(resource.getEmail());
        IdSecureUtils.verifyIdNotNull(resource.getId());
        // 根据该实体中的用户名去查找
        User userUsername = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, resource.getUsername()));
        // 根据该实体中的邮箱去查找
        User userEmail = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, AesUtils.encrypt(resource.getEmail())));

        // 验证用户名是否存在
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

        // 查看是否修改用户信息成功
        boolean result = this.saveOrUpdate(user);

        if (result) {
            // 新的用户的角色ID列表
            Set<Long> oldRoleIds = resource.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
            // 原来用户角色ID列表
            Set<Long> newRoleIds = roleMapper.findSetByUserId(resource.getId()).stream().map(Role::getId).collect(Collectors.toSet());

            // 如果角色集合不一致再进行更新
            if (CollUtils.isCollectionNotEqual(oldRoleIds, newRoleIds) && CollUtils.isNotBlank(resource.getRoles())) {
                // 删除所有角色
                usersRolesService.removeById(resource.getId());
                // 批量添加角色
                boolean flag = usersRolesService.saveBatch(
                        resource.getRoles().stream().map(i -> {
                            UsersRoles usersRoles = new UsersRoles();
                            usersRoles.setUserId(resource.getId());
                            usersRoles.setRoleId(i.getId());
                            return usersRoles;
                        }).collect(Collectors.toList())
                );

                if (flag) {
                    // 若用户修改了角色，要手动删除一下缓存
                    String keyPermissions = redisUtils.getRedisCommonsKey("Permission", oldUsername);
                    String keyRoles = redisUtils.getRedisCommonsKey("Roles", oldUsername);
                    if (redisUtils.hasKey(keyPermissions) && redisUtils.hasKey(keyRoles)) {
                        // 手动删除缓存
                        redisUtils.del(keyPermissions, keyRoles);
                        // 手动调用一次更新缓存
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
            map.put(I18nMessagesUtils.get("download.user.state"), userVO.isEnabled() ? "启用" : "禁用");
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
        // 根据用户名去查询是否存在用户
        User userUsername = this.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, resource.getUsername()));
        if (ObjectUtil.isNotNull(userUsername)) {
            throw new BadRequestException(I18nMessagesUtils.get("username.exists"));
        }
        // 根据邮箱去查询
        User userEmail = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, AesUtils.encrypt(resource.getEmail())));
        if (ObjectUtil.isNotNull(userEmail)) {
            throw new BadRequestException(I18nMessagesUtils.get("email.exists"));
        }
        // 转换实体
        User user = userVOConvert.toEntity(resource);
        // 设置默认密码
        user.setPassword(passwordEncoder.encode(sysSettingService.getSysSettings().getUserInitPassword()));
        // 保存
        boolean result = this.save(user);

        // 保存成功后再去查询一次获取主键
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
