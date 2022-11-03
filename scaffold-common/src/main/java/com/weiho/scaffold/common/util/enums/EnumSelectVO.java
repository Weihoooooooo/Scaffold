package com.weiho.scaffold.common.util.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/9/7
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@ApiModel("前端选择器VO对象")
public class EnumSelectVO {
    @ApiModelProperty("前端Option的value")
    private Integer value;

    @ApiModelProperty("前端Option的label")
    private String label;

    @ApiModelProperty("枚举类的name")
    private String name;
}
