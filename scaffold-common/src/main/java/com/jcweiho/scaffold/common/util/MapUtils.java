package com.jcweiho.scaffold.common.util;

import cn.hutool.core.map.MapUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/11/24
 */
@UtilityClass
public class MapUtils extends MapUtil {
    /**
     * 获取Map的Key数组
     *
     * @param map 传入的Map
     * @param <K> Key
     * @param <V> Value
     * @return Key的Set数组
     */
    public <K, V> Set<K> getKeySet(Map<K, V> map) {
        if (isEmpty(map)) {
            return CollUtils.empty(Set.class);
        }
        return map.keySet();
    }
}
