package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.weiho.scaffold.common.annotation.EnumConvertMethod;
import com.weiho.scaffold.common.util.enums.Enum;
import com.weiho.scaffold.common.util.enums.EnumUtils;
import com.weiho.scaffold.i18n.I18nKey;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/10/10
 */
@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MenuTypeEnum implements Enum, I18nKey {
    MENU(0, "顶级菜单", "enums.menu"),
    MENU_CHILDREN(1, "子菜单", "enum.menu.children"),
    PERMISSION(2, "权限菜单", "enums.menu.permission");

    @JsonValue
    @EnumValue
    private final Integer key;

    private final String display;

    private final String i18nKey;

    @EnumConvertMethod
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MenuTypeEnum convert(Integer key) {
        return EnumUtils.convertEnum(MenuTypeEnum.class, key);
    }

    public String getDisplay() {
        return I18nMessagesUtils.get(i18nKey);
    }
}
