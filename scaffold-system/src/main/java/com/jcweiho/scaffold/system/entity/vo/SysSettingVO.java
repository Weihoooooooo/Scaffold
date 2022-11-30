package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Weiho
 * @since 2022/11/1
 */
@ToString
@Data
@ApiModel("系统参数VO")
public class SysSettingVO {
    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("系统名称")
    @NotBlank(message = "系统名称不能为空!")
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
    @NotBlank(message = "系统Logo不能为空!")
    @Pattern(regexp = "[a-zA-z]+://[^\\s]*", message = "Logo的地址不正确")
    private String sysLogo;

    @ApiModelProperty("系统用户初始密码")
    private String userInitPassword;
}
