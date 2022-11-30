package com.jcweiho.scaffold.logging.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/9/13
 */
@Getter
@Setter
@ToString
@ApiModel(value = "LogUserVO对象", description = "用户中心VO")
public class LogUserVO extends LogErrorUserVO {
    @ApiModelProperty("消耗时间(ms)")
    private Long time;
}
