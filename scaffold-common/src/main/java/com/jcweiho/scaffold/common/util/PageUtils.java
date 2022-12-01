/* Copyright 2018 Elune,hu peng
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jcweiho.scaffold.common.util;

import cn.hutool.core.builder.GenericBuilder;
import lombok.experimental.UtilityClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页工具类
 */
@UtilityClass
public class PageUtils {
    /**
     * list 分页工具
     *
     * @param page 当前页面(从0开始)
     * @param size 每页显示的数量
     * @param list 传入要分页的数组
     * @param <L>  泛型
     * @return 分页后的list
     */
    public <L> List<L> toPage(int page, int size, List<L> list) {
        return CollUtils.page(page, size, list);
    }

    /**
     * 分页包装类
     *
     * @param object        Map的key
     * @param totalElements Map的value
     * @return Map<String, Object>
     */
    public Map<String, Object> toPageContainer(final Object object, final Object totalElements) {
        return GenericBuilder.of(LinkedHashMap<String, Object>::new)
                .with(Map::put, "content", object)
                .with(Map::put, "totalElements", totalElements).build();
    }
}
