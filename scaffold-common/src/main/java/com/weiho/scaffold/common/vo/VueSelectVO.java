package com.weiho.scaffold.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Weiho
 * @since 2022/11/14
 */
@Getter
@Setter
@ApiModel("前端选择器VO")
@AllArgsConstructor
public class VueSelectVO {
    @ApiModelProperty("前端Option的value")
    private Object value;

    @ApiModelProperty("前端Option的label")
    private String label;
}
