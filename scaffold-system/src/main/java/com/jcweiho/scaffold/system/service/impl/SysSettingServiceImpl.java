package com.jcweiho.scaffold.system.service.impl;

import com.jcweiho.scaffold.common.util.AesUtils;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.system.cache.service.CacheRefresh;
import com.jcweiho.scaffold.system.entity.SysSetting;
import com.jcweiho.scaffold.system.entity.convert.SysLogoTitleVOConvert;
import com.jcweiho.scaffold.system.entity.convert.SysSettingVOConvert;
import com.jcweiho.scaffold.system.entity.vo.SysLogoTitleVO;
import com.jcweiho.scaffold.system.entity.vo.SysSettingVO;
import com.jcweiho.scaffold.system.mapper.SysSettingMapper;
import com.jcweiho.scaffold.system.service.SysSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-09-19
 */
@Service
@RequiredArgsConstructor
public class SysSettingServiceImpl extends CommonServiceImpl<SysSettingMapper, SysSetting> implements SysSettingService {
    private final SysLogoTitleVOConvert sysLogoTitleVOConvert;
    private final SysSettingVOConvert sysSettingVOConvert;
    private final CacheRefresh cacheRefresh;

    @Override
    @Cacheable(value = "Scaffold:System", key = "'settings'")
    public SysSetting getSysSettings() {
        SysSetting sysSetting = this.list().get(0);
        sysSetting.setUserInitPassword(AesUtils.decrypt(sysSetting.getUserInitPassword()));
        return sysSetting;
    }

    @Override
    public Map<String, Object> getLogoAndTitle(HttpServletRequest request, SysSetting sysSetting) {
        Map<String, Object> result = new HashMap<>();
        SysLogoTitleVO sysLogoTitleVO = sysLogoTitleVOConvert.toPojo(sysSetting);
        result.put("logo", sysLogoTitleVO.getSysLogo());
        result.put("title", I18nMessagesUtils.getNameForI18n(request, sysLogoTitleVO));
        return result;
    }

    @Override
    public boolean updateSysSettings(SysSettingVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        SysSetting sysSetting = sysSettingVOConvert.toEntity(resources);
        sysSetting.setUserInitPassword(AesUtils.encrypt(sysSetting.getUserInitPassword()));
        boolean flag = this.saveOrUpdate(sysSetting);
        if (flag) {
            // 更新缓存
            cacheRefresh.updateSysSetting(sysSetting);
        }
        return flag;
    }
}
