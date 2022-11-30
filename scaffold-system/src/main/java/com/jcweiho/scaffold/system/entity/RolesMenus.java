package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/9/29
 */
@Data
@ApiModel("角色菜单中间表")
@TableName("roles_menus")
public class RolesMenus implements Serializable {
    @TableField("menu_id")
    @ApiModelProperty("菜单主键ID")
    private Long menuId;

    @ApiModelProperty("角色主键ID")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;
}
