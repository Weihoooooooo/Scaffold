package com.jcweiho.scaffold.system.service.impl;

import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.RolesMenus;
import com.jcweiho.scaffold.system.mapper.RolesMenusMapper;
import com.jcweiho.scaffold.system.service.RolesMenusService;
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
