package com.weiho.scaffold.system.entity.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weiho.scaffold.i18n.I18n;
import com.weiho.scaffold.system.entity.enums.MenuTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/8/9
 */
@Data
@ApiModel(value = "menu的DTO对象")
public class MenuDTO implements Serializable, I18n {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("组件名称")
    private String componentName;

    @ApiModelProperty("前端使用的path")
    private String path;

    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("菜单中文名字")
    private String nameZhCn;

    @ApiModelProperty("菜单中国香港名字")
    private String nameZhHk;

    @ApiModelProperty("菜单中国台湾名字")
    private String nameZhTw;

    @ApiModelProperty("菜单英文名字")
    private String nameEnUs;

    @ApiModelProperty("图标")
    private String iconCls;

    @ApiModelProperty("后端使用的url")
    private String url;

    @ApiModelProperty("菜单权限")
    private String permission;

    @ApiModelProperty("是否保持激活")
    private Boolean keepAlive;

    @ApiModelProperty("是否隐藏")
    private Boolean hidden;

    @ApiModelProperty("上级菜单ID")
    private Long parentId;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("类型 1-菜单项 2-权限菜单")
    private MenuTypeEnum type;

    @ApiModelProperty("排序")
    private Long sort;

    @ApiModelProperty("创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;

    @ApiModelProperty("子菜单集合")
    private List<MenuDTO> children;
}
