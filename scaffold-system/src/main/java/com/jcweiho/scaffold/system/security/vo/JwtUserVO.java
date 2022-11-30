package com.jcweiho.scaffold.system.security.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.system.entity.Avatar;
import com.jcweiho.scaffold.system.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 用户登录后接收Security的用户信息与权限
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Getter
@Setter
@ToString
@ApiModel("登录授权后用户实体")
public class JwtUserVO implements UserDetails {

    @IdEncrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("头像信息")
    private Avatar avatar;

    @ApiModelProperty("用户名")
    private String username;

    @JsonIgnore
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("性别 0-女 1-男")
    private SexEnum sex;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("状态：1启用 0禁用")
    private boolean enabled;

    @ApiModelProperty("最后修改密码时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastPassResetTime;

    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty("用户权限集合")
    private Collection<SimpleGrantedAuthority> authorities;

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Collection<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    public void setRoles(Collection<SimpleGrantedAuthority> roles) {
        this.authorities = roles;
    }
}
