package com.jcweiho.scaffold.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/8/9
 */
@Data
@ApiModel(value = "前端路由Meta对象", description = "前端路由表所需对象")
@AllArgsConstructor
public class MenuMetaVO implements Serializable {
    @ApiModelProperty("网页的标签页标题")
    private String title;

    @ApiModelProperty("菜单栏的icon样式")
    private String icon;
}
