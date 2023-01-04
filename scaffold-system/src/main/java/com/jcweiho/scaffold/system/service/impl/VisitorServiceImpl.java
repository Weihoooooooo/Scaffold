package com.jcweiho.scaffold.system.service.impl;

import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.util.FileUtils;
import com.jcweiho.scaffold.common.util.ListUtils;
import com.jcweiho.scaffold.common.util.PageUtils;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.entity.Visitor;
import com.jcweiho.scaffold.system.entity.convert.HouseholdVOConvert;
import com.jcweiho.scaffold.system.entity.convert.VisitorVOConvert;
import com.jcweiho.scaffold.system.entity.criteria.VisitorQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdVO;
import com.jcweiho.scaffold.system.entity.vo.VisitorVO;
import com.jcweiho.scaffold.system.mapper.VisitorMapper;
import com.jcweiho.scaffold.system.service.BuildingService;
import com.jcweiho.scaffold.system.service.HouseholdService;
import com.jcweiho.scaffold.system.service.OwnerService;
import com.jcweiho.scaffold.system.service.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 访客信息信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2023-01-02
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitorServiceImpl extends CommonServiceImpl<VisitorMapper, Visitor> implements VisitorService {
    private final VisitorVOConvert visitorVOConvert;
    private final BuildingService buildingService;
    private final HouseholdService householdService;
    private final HouseholdVOConvert householdVOConvert;
    private final OwnerService ownerService;

    @Override
    public List<VisitorVO> convertToVO(List<Visitor> visitors) {
        List<VisitorVO> visitorVOList = visitorVOConvert.toPojo(visitors);
        for (VisitorVO visitorVO : visitorVOList) {
            visitorVO.setBuildingNum(buildingService.getById(visitorVO.getBuildingId()).getBuildingNum());
            HouseholdVO householdVO = householdVOConvert.toPojo(householdService.getById(visitorVO.getHouseholdId()));
            householdVO.setOwnerName(ownerService.getById(householdVO.getOwnerId()).getName());
            visitorVO.setHouseholdVO(householdVO);
        }
        return visitorVOList;
    }

    @Override
    public List<Visitor> findAll(VisitorQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Visitor.class, criteria)));
    }


    @Override
    public Map<String, Object> findAll(VisitorQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Visitor> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(List<VisitorVO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (VisitorVO visitorVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.visitor.name"), visitorVO.getName());
            map.put(I18nMessagesUtils.get("download.visitor.phone"), visitorVO.getPhone());
            map.put(I18nMessagesUtils.get("download.visitor.building"), visitorVO.getBuildingNum());
            map.put(I18nMessagesUtils.get("download.visitor.household"), visitorVO.getHouseholdVO().getIdentityId());
            map.put(I18nMessagesUtils.get("download.createTime"), visitorVO.getCreateTime());
            map.put(I18nMessagesUtils.get("download.updateTime"), visitorVO.getUpdateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVisitor(VisitorVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        Visitor visitor = this.getById(resources.getId());
        // 添加修改的逻辑代码
        visitor.setName(resources.getName());
        visitor.setPhone(resources.getPhone());
        visitor.setBuildingId(resources.getBuildingId());
        visitor.setHouseholdId(resources.getHouseholdId());
        return this.saveOrUpdate(visitor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addVisitor(VisitorVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        return this.save(visitorVOConvert.toEntity(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVisitor(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
