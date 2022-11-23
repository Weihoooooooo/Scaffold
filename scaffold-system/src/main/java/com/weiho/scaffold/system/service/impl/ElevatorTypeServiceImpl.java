package com.weiho.scaffold.system.service.impl;

import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.ElevatorType;
import com.weiho.scaffold.system.mapper.ElevatorTypeMapper;
import com.weiho.scaffold.system.service.ElevatorTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ElevatorTypeServiceImpl extends CommonServiceImpl<ElevatorTypeMapper, ElevatorType> implements ElevatorTypeService {
    @Override
    public Set<ElevatorType> findSetByElevatorId(Long elevatorId) {
        return this.getBaseMapper().findSetByElevatorId(elevatorId);
    }

    @Override
    public List<VueSelectVO> getElevatorTypeSelect(HttpServletRequest request) {
        List<VueSelectVO> list = new ArrayList<>();
        List<ElevatorType> elevatorTypes = this.list();
        for (ElevatorType elevatorType : elevatorTypes) {
            list.add(new VueSelectVO(IdSecureUtils.des().encrypt(elevatorType.getId()), I18nMessagesUtils.getNameForI18n(request, elevatorType)));
        }
        return list;
    }
}
