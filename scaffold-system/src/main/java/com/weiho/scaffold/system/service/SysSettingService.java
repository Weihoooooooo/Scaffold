package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.SysSetting;
import com.weiho.scaffold.system.entity.vo.SysSettingVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-09-19
 */
public interface SysSettingService extends CommonService<SysSetting> {
    /**
     * 获取系统设置
     *
     * @return /
     */
    SysSetting getSysSettings();

    /**
     * 获取系统Logo和title
     *
     * @param request    请求对象
     * @param sysSetting 系统设置
     * @return /
     */
    Map<String, Object> getLogoAndTitle(HttpServletRequest request, SysSetting sysSetting);

    /**
     * 修改系统设置
     *
     * @param resources 新的配置
     * @return /
     */
    boolean updateSysSettings(SysSettingVO resources);
}
