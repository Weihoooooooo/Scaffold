package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.system.entity.Menu;
import com.jcweiho.scaffold.system.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/9/28
 */
@Setter
@Getter
@ToString
@ApiModel("角色VO")
public class RoleVO extends Role implements Serializable {

    @ApiModelProperty("能访问的菜单集合")
    private Set<Menu> menus;

}
