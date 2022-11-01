package com.weiho.scaffold.system.service.impl;

import com.weiho.scaffold.common.util.aes.AesUtils;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.cache.service.CacheRefresh;
import com.weiho.scaffold.system.entity.SysSetting;
import com.weiho.scaffold.system.entity.convert.SysLogoTitleVOConvert;
import com.weiho.scaffold.system.entity.convert.SysSettingVOConvert;
import com.weiho.scaffold.system.entity.vo.SysLogoTitleVO;
import com.weiho.scaffold.system.entity.vo.SysSettingVO;
import com.weiho.scaffold.system.mapper.SysSettingMapper;
import com.weiho.scaffold.system.service.SysSettingService;
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
        return this.list().get(0);
    }

    @Override
    public Map<String, Object> getLogoAndTitle(HttpServletRequest request, SysSetting sysSetting) {
        Map<String, Object> result = new HashMap<>();
        SysLogoTitleVO sysLogoTitleVO = sysLogoTitleVOConvert.toPojo(sysSetting);
        result.put("logo", sysLogoTitleVO.getSysLogo());
        String language = request.getHeader("Accept-Language") == null ? "zh-CN" : request.getHeader("Accept-Language");
        switch (language) {
            case "zh-CN":
                result.put("title", sysLogoTitleVO.getSysNameZhCn());
                break;
            case "zh-HK":
                result.put("title", sysLogoTitleVO.getSysNameZhHk());
                break;
            case "zh-TW":
                result.put("title", sysLogoTitleVO.getSysNameZhTw());
                break;
            case "en-US":
                result.put("title", sysLogoTitleVO.getSysNameEnUs());
                break;
            default:
                result.put("title", sysLogoTitleVO.getSysName());
                break;
        }
        return result;
    }

    @Override
    public boolean updateSysSettings(SysSettingVO resources) {
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
