package com.jcweiho.scaffold.system.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/8/9
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel(value = "前端路由VO", description = "前端路由表所需对象")
public class MenuVO implements Serializable {
    @ApiModelProperty("菜单名称")
    private String name;

    @ApiModelProperty("前端使用的路径")
    private String path;

    @ApiModelProperty("菜单是否隐藏")
    private Boolean hidden;

    @ApiModelProperty("是否重定向")
    private String redirect;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("Meta对象")
    private MenuMetaVO meta;

    @ApiModelProperty("子菜单列表")
    private List<MenuVO> children;
}
