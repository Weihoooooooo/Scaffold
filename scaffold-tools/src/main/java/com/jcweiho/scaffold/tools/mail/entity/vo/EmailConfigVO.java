package com.jcweiho.scaffold.tools.mail.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author Weiho
 * @since 2022/9/5
 */
@Getter
@Setter
@ToString
@ApiModel(value = "邮件发送配置对象")
public class EmailConfigVO {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("发件人邮箱")
    private String fromUser;

    @ApiModelProperty("邮件服务器SMTP地址")
    @NotBlank(message = "邮件服务器SMTP地址不能为空！")
    private String host;

    @ApiModelProperty("端口")
    @NotBlank(message = "端口不能为空！")
    private String port;

    @ApiModelProperty("授权码")
    @NotBlank(message = "授权码不能为空！")
    private String pass;

    @ApiModelProperty("发送人名称")
    private String username;
}
