package com.jcweiho.scaffold.system.entity.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcweiho.scaffold.common.annotation.Desensitize;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.common.sensitive.enums.DesensitizeStrategy;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.system.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/10/21
 */
@Getter
@Setter
@ToString
@ApiModel("业主VO")
public class OwnerVO extends CommonEntity implements Serializable {
    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("业主姓名")
    @NotBlank(message = "业主姓名不能为空！")
    @Desensitize(DesensitizeStrategy.USERNAME)
    @Xss
    private String name;

    @ApiModelProperty("业主手机号码")
    @NotBlank(message = "手机号不能为空！")
    @Pattern(regexp = "^(13\\d|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18\\d|19[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    @Xss
    private String phone;

    @ApiModelProperty("业主身份证号")
    @NotBlank(message = "身份证号不能为空！")
    @Desensitize(DesensitizeStrategy.ID_CARD)
    @Xss
    private String identityId;

    @ApiModelProperty("业主邮箱")
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空！")
    @Xss
    private String email;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty("业主密码")
    @Xss
    private String password;

    @ApiModelProperty("性别 0-女 1-男")
    private SexEnum sex;
}
