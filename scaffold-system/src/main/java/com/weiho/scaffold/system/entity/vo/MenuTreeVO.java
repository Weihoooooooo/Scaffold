package com.weiho.scaffold.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/11/13
 */
@Getter
@Setter
@ToString
@ApiModel("菜单树VO")
public class MenuTreeVO implements Serializable {
    @ApiModelProperty("菜单主键")
    private Long id;

    @ApiModelProperty("菜单名字")
    private String label;

    @ApiModelProperty("子菜单列表")
    private List<MenuTreeVO> children;
}
