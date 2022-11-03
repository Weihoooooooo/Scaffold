package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.weiho.scaffold.common.util.enums.EnumSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Getter
@RequiredArgsConstructor
public enum NoticeToEnum implements EnumSelect {
    ALL_OWNER(0, "全体业主"),
    ALL_USER(1, "全体员工"),
    ALL(2, "全体人员");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;
}
