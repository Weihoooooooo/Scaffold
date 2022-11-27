package com.weiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.exception.SecurityException;
import com.weiho.scaffold.common.util.*;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.enums.SortTypeEnum;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Menu;
import com.weiho.scaffold.system.entity.Role;
import com.weiho.scaffold.system.entity.RolesMenus;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.entity.convert.RoleDTOConvert;
import com.weiho.scaffold.system.entity.convert.RoleVOConvert;
import com.weiho.scaffold.system.entity.criteria.RoleQueryCriteria;
import com.weiho.scaffold.system.entity.dto.RoleDTO;
import com.weiho.scaffold.system.entity.vo.RoleVO;
import com.weiho.scaffold.system.mapper.RoleMapper;
import com.weiho.scaffold.system.service.MenuService;
import com.weiho.scaffold.system.service.RoleService;
import com.weiho.scaffold.system.service.RolesMenusService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends CommonServiceImpl<RoleMapper, Role> implements RoleService {
    private final MenuService menuService;
    private final RoleDTOConvert roleDTOConvert;
    private final RoleVOConvert roleVOConvert;
    private final RolesMenusService rolesMenusService;

    @Override
    @Cacheable(value = "Scaffold:Commons:Permission", key = "'loadPermissionByUsername:' + #p1", unless = "#result.size() <= 1")
    public Collection<SimpleGrantedAuthority> mapToGrantedAuthorities(Long userId, String username) {
        //获取用户角色集合
        Set<Role> roles = this.getBaseMapper().findSetByUserId(userId);
        Set<RoleDTO> roleDTOS = new HashSet<>();
        for (Role role : roles) {
            //转化DTO对象
            RoleDTO roleDTO = roleDTOConvert.toPojo(role);
            //根据角色ID获取menu集合
            Set<Menu> menuSet = menuService.findSetByRoleId(role.getId());
            //放入DTO对象
            roleDTO.setMenus(menuSet);
            //加入DTO对象集合
            roleDTOS.add(roleDTO);
        }
        //新建权限集合，将DTO对象集合中的permission抽出来作为新的集合
        Set<String> permissions = roleDTOS.stream().map(RoleDTO::getPermission).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        //将menu中的权限permission添加进去
        permissions.addAll(
                roleDTOS.stream().flatMap(roleDTO -> roleDTO.getMenus().stream())
                        .map(Menu::getPermission)
                        .filter(StringUtils::isNotBlank).collect(Collectors.toSet())
        );
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "Scaffold:Commons:Roles", key = "'loadRolesByUsername:' + #p0.getUsername()")
    public List<Role> findListByUser(User user) {
        List<Role> roles = this.getBaseMapper().findListByUserId(user.getId());
        // 没角色则给最低权限
        if (CollUtils.isEmpty(roles)) {
            // 获取所有角色中等级最低的
            Integer minLevel = CollUtils.max(this.list().stream().map(Role::getLevel).collect(Collectors.toSet()));
            // 将最低等级的角色实体放入
            roles.add(this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getLevel, minLevel)));
        }
        return roles;
    }

    @Override
    public Map<String, Object> findAll(RoleQueryCriteria criteria, Pageable pageable, HttpServletRequest request) {
        startPage(pageable, "level", SortTypeEnum.ASC);
        PageInfo<Role> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToDTOForLanguage(pageInfo.getList(), request), pageInfo.getTotal());
    }

    @Override
    public Map<String, Object> findAll(RoleQueryCriteria criteria, Pageable pageable) {
        startPage(pageable, "level", SortTypeEnum.ASC);
        PageInfo<Role> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public Integer findHighLevel(Long userId) {
        List<Role> roles = this.getBaseMapper().findListByUserId(userId);
        if (roles.size() == 0) {
            return 99;// 避免当用户还没角色时候报警告
        } else {
            return CollUtils.min(roles.stream().map(Role::getLevel).collect(Collectors.toList()));
        }
    }

    @Override
    public void checkLevel(Long resourceId) {
        if (ObjectUtil.isNotNull(resourceId)) {
            Integer resourceUserLevel = this.findHighLevel(resourceId);
            Integer securityUserLevel = this.findHighLevel(SecurityUtils.getUserId());
            if (resourceUserLevel < securityUserLevel) {
                throw new SecurityException(ResultCodeEnum.FAILED, I18nMessagesUtils.get("permission.none.tip"));
            }
        }
    }

    @Override
    public void checkLevel(Set<Role> roles) {
        Set<Integer> integers = new HashSet<>();
        Set<Long> ids = roles.stream().map(Role::getId).collect(Collectors.toSet());
        for (Long id : ids) {
            integers.add(this.getBaseMapper().selectById(id).getLevel());
        }
        Integer securityUserLevel = this.findHighLevel(SecurityUtils.getUserId());
        Integer rolesUserLevel = CollUtils.min(integers);
        if (rolesUserLevel < securityUserLevel) {
            throw new SecurityException(ResultCodeEnum.FAILED, I18nMessagesUtils.get("permission.none.tip"));
        }
    }

    @Override
    public void checkLevel(Integer level) {
        // 获取当前登录用户的最高级别
        Integer heightLevel = this.findHighLevel(SecurityUtils.getUserId());
        if (ObjectUtil.isNotNull(level)) {
            if (level < heightLevel) {
                throw new BadRequestException(I18nMessagesUtils.get("role.not.ok1") + heightLevel + I18nMessagesUtils.get("role.not.ok2") + level);
            }
        }
    }

    @Override
    public Map<String, Integer> getLevelScope() {
        List<Role> roles = this.list();
        return new LinkedHashMap<String, Integer>(2) {{
            put("max", CollUtils.max(roles.stream().map(Role::getLevel).collect(Collectors.toList())));
            put("min", CollUtils.min(roles.stream().map(Role::getLevel).collect(Collectors.toList())));
        }};
    }

    @Override
    public void download(List<RoleDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDTO roleDTO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.role.name"), roleDTO.getName());
            map.put(I18nMessagesUtils.get("download.role.level"), roleDTO.getLevel());
            map.put(I18nMessagesUtils.get("download.role.permission"), roleDTO.getPermission());
            map.put(I18nMessagesUtils.get("download.updateTime"), roleDTO.getUpdateTime());
            map.put(I18nMessagesUtils.get("download.createTime"), roleDTO.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    public List<Role> findAll(RoleQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Role.class, criteria)));
    }

    @Override
    public List<RoleVO> convertToVO(List<Role> roles) {
        List<RoleVO> roleVOS = roleVOConvert.toPojo(roles);
        for (RoleVO roleVO : roleVOS) {
            // 过滤父节点菜单，避免前端的菜单树多选出现Bug
            roleVO.setMenus(menuService.findSetByRoleId(roleVO.getId()).stream().filter(m -> m.getParentId() != 0L).collect(Collectors.toSet()));
        }
        return roleVOS;
    }

    @Override
    public List<RoleDTO> convertToDTOForLanguage(List<Role> roles, HttpServletRequest request) {
        List<RoleDTO> roleDTOS = roleDTOConvert.toPojo(roles);
        for (int i = 0; i < roleDTOS.size(); i++) {
            for (int j = 0; j < roles.size(); j++) {
                roleDTOS.get(i).setName(I18nMessagesUtils.getNameForI18n(request, roles.get(i)));
            }
        }
        return roleDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role resource) {
        IdSecureUtils.verifyIdNotNull(resource.getId());
        Role role = this.getById(resource.getId());

        // 根据角色名查找
        Role roleForName = this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getName, resource.getName()));
        if (ObjectUtil.isNotNull(roleForName) && !roleForName.getId().equals(role.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("role.exist.error"));
        }

        Role roleForPermission = this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getPermission, resource.getPermission()));
        if (ObjectUtil.isNotNull(roleForPermission) && !roleForPermission.getId().equals(resource.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("role.permission.exist.error"));
        }

        role.setName(resource.getName());
        role.setPermission(resource.getPermission());
        role.setLevel(resource.getLevel());
        role.setNameZhCn(resource.getNameZhCn());
        role.setNameZhHk(resource.getNameZhHk());
        role.setNameZhTw(resource.getNameZhTw());
        role.setNameEnUs(resource.getNameEnUs());

        return this.saveOrUpdate(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Set<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public RoleVO findById(Long id) {
        IdSecureUtils.verifyIdNotNull(id);
        Role role = this.getById(id);
        RoleVO roleVO = roleVOConvert.toPojo(role);
        roleVO.setMenus(menuService.findSetByRoleId(roleVO.getId()));
        return roleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(RoleVO resource) {
        IdSecureUtils.verifyIdNotNull(resource.getId());
        if (CollUtils.isNotBlank(resource.getMenus())) {
            rolesMenusService.removeById(resource.getId());
            return rolesMenusService.saveBatch(
                    resource.getMenus().stream().map(i -> {
                        RolesMenus rolesMenus = new RolesMenus();
                        rolesMenus.setRoleId(resource.getId());
                        rolesMenus.setMenuId(i.getId());
                        return rolesMenus;
                    }).collect(Collectors.toList())
            );
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role resource) {
        IdSecureUtils.verifyIdNull(resource.getId());
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getName, resource.getName())))) {
            throw new BadRequestException(I18nMessagesUtils.get("role.exist.error"));
        }
        return this.save(resource);
    }
}
