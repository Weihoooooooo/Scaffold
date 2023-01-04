package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Village;

/**
 * <p>
 * 小区基本信息参数 服务类
 * </p>
 *
 * @author Weiho
 * @since 2023-01-04
 */
public interface VillageService extends CommonService<Village> {
    /**
     * 获取小区参数
     *
     * @return 小区参数
     */
    Village getVillage();

    /**
     * 更新小区参数
     *
     * @param resources 新的小区参数
     * @return 是否修改成功
     */
    boolean updateVillage(Village resources);
}
