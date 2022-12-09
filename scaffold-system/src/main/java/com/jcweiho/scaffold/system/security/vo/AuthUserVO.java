package com.jcweiho.scaffold.system.security.vo;

import com.jcweiho.scaffold.common.annotation.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 前端登录对象
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Getter
@Setter
@ApiModel("前端登录VO对象")
public class AuthUserVO {
    @Xss
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @Xss
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    @Xss
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    @Xss
    @ApiModelProperty(value = "UUID", required = true)
    private String uuid;

    @Override
    public String toString() {
        return "AuthUserVO{username=" + username + ", password= ******}";
    }
}
