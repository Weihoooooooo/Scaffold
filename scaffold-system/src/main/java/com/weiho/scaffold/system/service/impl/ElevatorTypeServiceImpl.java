package com.weiho.scaffold.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.FileUtils;
import com.weiho.scaffold.common.util.ListUtils;
import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.ElevatorType;
import com.weiho.scaffold.system.entity.criteria.ElevatorTypeQueryCriteria;
import com.weiho.scaffold.system.mapper.ElevatorTypeMapper;
import com.weiho.scaffold.system.service.ElevatorTypeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        List<VueSelectVO> list = ListUtils.list(false);
        List<ElevatorType> elevatorTypes = this.list();
        for (ElevatorType elevatorType : elevatorTypes) {
            list.add(new VueSelectVO(IdSecureUtils.des().encrypt(elevatorType.getId()),
                    I18nMessagesUtils.getNameForI18n(request, elevatorType)));
        }
        return list;
    }

    @Override
    public List<ElevatorType> findAll(ElevatorTypeQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(ElevatorType.class, criteria)));
    }

    @Override
    public Map<String, Object> findAll(ElevatorTypeQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        return toPageContainer(this.findAll(criteria));
    }

    @Override
    public void download(List<ElevatorType> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (ElevatorType elevatorType : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("电梯类型", elevatorType.getName());
            map.put("电梯类型zh-CN", elevatorType.getNameZhCn());
            map.put("电梯类型zh-HK", elevatorType.getNameZhHk());
            map.put("电梯类型zh-TW", elevatorType.getNameZhHk());
            map.put("电梯类型en-US", elevatorType.getNameEnUs());
            map.put("创建时间", elevatorType.getCreateTime());
            map.put(I18nMessagesUtils.get("download.createTime"), elevatorType.getCreateTime());
            map.put(I18nMessagesUtils.get("download.updateTime"), elevatorType.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateElevatorType(ElevatorType resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        ElevatorType elevatorType = this.getById(resources.getId());
        // 根据电梯类型查找
        ElevatorType elevatorTypeName = this.getOne(new LambdaQueryWrapper<ElevatorType>().eq(ElevatorType::getName, resources.getName()));
        if (ObjectUtil.isNotNull(elevatorTypeName) && !elevatorTypeName.getId().equals(elevatorType.getId())) {
            throw new BadRequestException("该电梯类型名已存在！");
        }
        elevatorType.setName(resources.getName());
        elevatorType.setNameZhCn(resources.getNameZhCn());
        elevatorType.setNameZhHk(resources.getNameZhHk());
        elevatorType.setNameEnUs(resources.getNameEnUs());
        elevatorType.setNameZhTw(resources.getNameZhTw());
        return this.saveOrUpdate(elevatorType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addElevatorType(ElevatorType resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        if (ObjectUtil.isNotNull(this.getOne(new LambdaQueryWrapper<ElevatorType>().eq(ElevatorType::getName, resources.getName())))) {
            throw new BadRequestException("该电梯类型名已存在！");
        }
        return this.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteElevatorType(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
