package com.weiho.scaffold.system.service.impl;

import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.UsersRoles;
import com.weiho.scaffold.system.mapper.UsersRolesMapper;
import com.weiho.scaffold.system.service.UsersRolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Weiho
 * @since 2022/11/19
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UsersRolesServiceImpl extends CommonServiceImpl<UsersRolesMapper, UsersRoles> implements UsersRolesService {
}
