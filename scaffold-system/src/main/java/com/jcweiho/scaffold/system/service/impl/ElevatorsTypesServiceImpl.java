package com.jcweiho.scaffold.system.service.impl;

import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.ElevatorsTypes;
import com.jcweiho.scaffold.system.mapper.ElevatorsTypesMapper;
import com.jcweiho.scaffold.system.service.ElevatorsTypesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Weiho
 * @since 2022/11/19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ElevatorsTypesServiceImpl extends CommonServiceImpl<ElevatorsTypesMapper, ElevatorsTypes> implements ElevatorsTypesService {
}
