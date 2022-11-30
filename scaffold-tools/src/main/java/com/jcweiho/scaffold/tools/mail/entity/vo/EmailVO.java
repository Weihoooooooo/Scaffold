package com.jcweiho.scaffold.tools.mail.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/9/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("发送邮件接收参数对象")
public class EmailVO {
    @NotEmpty(message = "收件人不能为空")
    @ApiModelProperty(value = "收件人支持多个收件人", required = true)
    private List<String> tos;

    @NotBlank(message = "主题不能为空")
    @ApiModelProperty(value = "邮件主题", required = true)
    private String subject;

    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "邮件内容", required = true)
    private String content;
}
