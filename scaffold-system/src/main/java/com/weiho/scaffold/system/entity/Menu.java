package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Menu对象", description = "系统菜单表")
@TableName("menu")
public class Menu extends CommonEntity {

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("组件路径")
    @TableField("component")
    private String component;

    @ApiModelProperty("组件名称")
    @TableField("component_name")
    private String componentName;

    @ApiModelProperty("前端使用的path")
    @TableField("path")
    private String path;

    @ApiModelProperty("菜单名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("菜单中文名字")
    @TableField("name_zh_cn")
    private String nameZhCn;

    @ApiModelProperty("菜单中国香港名字")
    @TableField("name_zh_hk")
    private String nameZhHk;

    @ApiModelProperty("菜单中国台湾名字")
    @TableField("name_zh_tw")
    private String nameZhTw;

    @ApiModelProperty("菜单英文名字")
    @TableField("name_en_us")
    private String nameEnUs;

    @ApiModelProperty("图标")
    @TableField("icon_cls")
    private String iconCls;

    @ApiModelProperty("后端使用的url")
    @TableField("url")
    private String url;

    @ApiModelProperty("菜单权限")
    @TableField("permission")
    private String permission;

    @ApiModelProperty("是否保持激活")
    @TableField("keep_alive")
    private Long keepAlive;

    @ApiModelProperty("是否隐藏")
    @TableField("hidden")
    private boolean hidden;

    @ApiModelProperty("上级菜单ID")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty("是否启用")
    @TableField("enabled")
    private boolean enabled;

    @ApiModelProperty("类型 1-菜单项 2-权限菜单")
    @TableField("type")
    private int type;

    @ApiModelProperty("排序")
    @TableField("sort")
    private Long sort;
}
