package com.weiho.scaffold.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Weiho
 * @since 2022/9/5
 */
@Data
@ApiModel("用户修改密码VO")
public class UserPassVO {
    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
}
