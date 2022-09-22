package com.weiho.scaffold.system.entity.vo;

import com.weiho.scaffold.system.entity.Avatar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/9/22
 */
@ApiModel("头像实体VO")
@Getter
@Setter
@ToString(callSuper = true)
public class AvatarVO extends Avatar implements Serializable {
    @ApiModelProperty("头像所属用户")
    private String username;
}
