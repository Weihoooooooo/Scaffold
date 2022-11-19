package com.weiho.scaffold.system.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.weiho.scaffold.common.util.enums.EnumSelect;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Weiho
 * @since 2022/11/19
 */
@Getter
@RequiredArgsConstructor
public enum ElevatorComputerRoomEnum implements EnumSelect {
    HAS_COMPUTER_ROOM(1, "有机房"),
    NO_COMPUTER_ROOM(0, "无机房");

    @EnumValue
    private final Integer key;

    @JsonValue
    private final String display;
}
