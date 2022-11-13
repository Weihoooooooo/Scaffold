package com.weiho.scaffold.system.entity.dto;

import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/8/4
 */
@Getter
@Setter
@ToString
@ApiModel(value = "RoleDTO对象")
public class RoleDTO extends CommonEntity implements Serializable {

    @IdEncrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色级别")
    private Integer level;

    @ApiModelProperty("功能权限")
    private String permission;

    @ApiModelProperty("能访问的菜单集合")
    private Set<Menu> menus;
}
