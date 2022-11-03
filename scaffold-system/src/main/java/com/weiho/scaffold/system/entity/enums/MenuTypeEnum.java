package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.weiho.scaffold.common.util.enums.EnumSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/10/10
 */
@Getter
@RequiredArgsConstructor
public enum MenuTypeEnum implements EnumSelect {
    MENU(0, "顶级菜单"),
    MENU_CHILDREN(1, "子菜单"),
    PERMISSION(2, "权限菜单");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;
}
