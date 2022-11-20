package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.i18n.I18n;
import com.weiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author Weiho
 * @since 2022-08-04
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel(value = "Role对象", description = "系统角色表")
@TableName("role")
public class Role extends CommonEntity implements I18n {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色名称")
    @TableField("name")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty("角色中文名称")
    @TableField("name_zh_cn")
    @NotBlank(message = "角色中文名称不能为空")
    private String nameZhCn;

    @ApiModelProperty("角色中国香港名称")
    @TableField("name_zh_hk")
    @NotBlank(message = "角色中国香港名称不能为空")
    private String nameZhHk;

    @ApiModelProperty("角色中国台湾名称")
    @TableField("name_zh_tw")
    @NotBlank(message = "角色中国台湾名称不能为空")
    private String nameZhTw;

    @ApiModelProperty("角色英文名称")
    @TableField("name_en_us")
    @NotBlank(message = "角色英文名称不能为空")
    private String nameEnUs;

    @ApiModelProperty("角色级别")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("功能权限")
    @TableField("permission")
    @NotBlank(message = "功能权限不能为空")
    private String permission;

}
