package com.weiho.scaffold.common.util;

import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Iterator;

/**
 * 集合操作工具类
 *
 * @author Weiho
 * @since 2022/9/22
 */
@UtilityClass
public class CollUtils extends CollUtil {

    /**
     * 判断两个集合是否不相等
     *
     * @param set1 集合1
     * @param set2 集合2
     * @return 是否相等
     */
    // 判断两个Set中的元素是否一致
    public <T> boolean isCollectionEqual(Collection<T> set1, Collection<T> set2) {
        if (isEmpty(set1) && isEmpty(set2)) {
            return true;
        }
        if (isEmpty(set1) || isEmpty(set2) || set1.size() != set2.size()) {
            return false;
        }
        Iterator<T> ite1 = set1.iterator();
        Iterator<T> ite2 = set2.iterator();
        boolean isFullEqual = true;
        while (ite2.hasNext()) {
            if (!set1.contains(ite2.next())) {
                isFullEqual = false;
            }
        }
        return isFullEqual;
    }

    /**
     * 判断两个集合是否不相等
     *
     * @param set1 集合1
     * @param set2 集合2
     * @return 是否不相等
     */
    public <T> boolean isCollectionNotEqual(Collection<T> set1, Collection<T> set2) {
        return !isCollectionEqual(set1, set2);
    }
}
