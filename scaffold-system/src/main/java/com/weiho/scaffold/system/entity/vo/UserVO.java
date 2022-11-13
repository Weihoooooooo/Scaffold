package com.weiho.scaffold.system.entity.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.Avatar;
import com.weiho.scaffold.system.entity.Role;
import com.weiho.scaffold.system.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/9/14
 */
@Getter
@Setter
@ToString
@ApiModel("用户VO")
@NoArgsConstructor
public class UserVO extends CommonEntity implements Serializable {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("头像")
    private Avatar avatar;

    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别 0-女 1-男")
    private SexEnum sex;

    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty("手机号码")
    @Pattern(regexp = "^(13\\d|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18\\d|19[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    @ApiModelProperty("状态：1启用 0禁用")
    private boolean enabled;

    @ApiModelProperty("最后修改密码时间")
    private Date lastPassResetTime;

    @ApiModelProperty(value = "权限集合", hidden = true)
    private Set<Role> roles;
}
