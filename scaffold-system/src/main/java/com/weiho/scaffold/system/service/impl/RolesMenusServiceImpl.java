package com.weiho.scaffold.system.service.impl;

import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.RolesMenus;
import com.weiho.scaffold.system.mapper.RolesMenusMapper;
import com.weiho.scaffold.system.service.RolesMenusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Weiho
 * @since 2022/11/19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RolesMenusServiceImpl extends CommonServiceImpl<RolesMenusMapper, RolesMenus> implements RolesMenusService {
}
