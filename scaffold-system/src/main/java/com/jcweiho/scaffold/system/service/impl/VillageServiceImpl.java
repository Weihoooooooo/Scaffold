package com.jcweiho.scaffold.system.service.impl;

import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.cache.service.CacheRefresh;
import com.jcweiho.scaffold.system.entity.Village;
import com.jcweiho.scaffold.system.mapper.VillageMapper;
import com.jcweiho.scaffold.system.service.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 小区基本信息参数 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2023-01-04
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VillageServiceImpl extends CommonServiceImpl<VillageMapper, Village> implements VillageService {
    private final CacheRefresh cacheRefresh;

    @Override
    @Cacheable(value = "Scaffold:Village", key = "'settings'")
    public Village getVillage() {
        return this.list().get(0);
    }

    @Override
    public boolean updateVillage(Village resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        boolean flag = this.saveOrUpdate(resources);
        if (flag) {
            cacheRefresh.updateVillage(resources);
        }
        return flag;
    }

}
