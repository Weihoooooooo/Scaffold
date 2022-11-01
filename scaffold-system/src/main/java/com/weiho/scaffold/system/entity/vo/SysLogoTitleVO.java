package com.weiho.scaffold.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/9/19
 */
@Getter
@Setter
@ToString
@ApiModel(value = "SysSettingVO")
public class SysLogoTitleVO {
    @ApiModelProperty("系统名称")
    private String sysName;

    @ApiModelProperty("系统名称中文")
    private String sysNameZhCn;

    @ApiModelProperty("系统名称中国香港")
    private String sysNameZhHk;

    @ApiModelProperty("系统名称中国台湾")
    private String sysNameZhTw;

    @ApiModelProperty("系统名称英文")
    private String sysNameEnUs;

    @ApiModelProperty("系统logo")
    private String sysLogo;
}
