package com.weiho.scaffold.system.service.impl;

import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Owner;
import com.weiho.scaffold.system.mapper.OwnerMapper;
import com.weiho.scaffold.system.service.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 业主表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OwnerServiceImpl extends CommonServiceImpl<OwnerMapper, Owner> implements OwnerService {

}
