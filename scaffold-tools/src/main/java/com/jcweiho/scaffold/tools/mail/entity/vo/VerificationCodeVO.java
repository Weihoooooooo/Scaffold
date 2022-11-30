package com.jcweiho.scaffold.tools.mail.entity.vo;

import com.jcweiho.scaffold.tools.mail.enums.EmailTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Weiho
 * @since 2022/9/6
 */
@Data
@ApiModel("发送邮箱验证码参数对象")
public class VerificationCodeVO {
    @NotBlank(message = "邮箱账号不能为空")
    @ApiModelProperty(value = "要发送验证码的邮箱账号", required = true)
    private String account;

    @ApiModelProperty(value = "要发送验证码的邮箱的后缀", required = true)
    private EmailTypeEnum suffix;
}
