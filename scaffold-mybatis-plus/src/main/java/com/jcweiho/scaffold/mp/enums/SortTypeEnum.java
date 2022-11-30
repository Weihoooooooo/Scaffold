package com.jcweiho.scaffold.mp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分页类型枚举
 *
 * @author Weiho
 */
@Getter
@AllArgsConstructor
public enum SortTypeEnum {
    ASC("asc"),
    DESC("desc");

    private final String sort;
}
