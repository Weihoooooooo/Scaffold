package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.i18n.I18n;
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
@ApiModel(value = "系统Logo和标题VO")
public class SysLogoTitleVO implements I18n {
    @ApiModelProperty("系统名称")
    private String name;

    @ApiModelProperty("系统名称中文")
    private String nameZhCn;

    @ApiModelProperty("系统名称中国香港")
    private String nameZhHk;

    @ApiModelProperty("系统名称中国台湾")
    private String nameZhTw;

    @ApiModelProperty("系统名称英文")
    private String nameEnUs;

    @ApiModelProperty("系统logo")
    private String sysLogo;
}
