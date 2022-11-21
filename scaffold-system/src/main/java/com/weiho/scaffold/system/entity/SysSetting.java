package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.mp.handler.EncryptHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 系统参数表
 * </p>
 *
 * @author Weiho
 * @since 2022-09-19
 */
@Getter
@Setter
@ToString
@TableName(value = "sys_setting", autoResultMap = true)
@ApiModel(value = "SysSetting对象", description = "系统参数表")
public class SysSetting extends CommonEntity {

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("系统名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("系统名称中文")
    @TableField("name_zh_cn")
    private String nameZhCn;

    @ApiModelProperty("系统名称中国香港")
    @TableField("name_zh_hk")
    private String nameZhHk;

    @ApiModelProperty("系统名称中国台湾")
    @TableField("name_zh_tw")
    private String nameZhTw;

    @ApiModelProperty("系统名称英文")
    @TableField("name_en_us")
    private String nameEnUs;

    @ApiModelProperty("系统logo")
    @TableField("sys_logo")
    private String sysLogo;

    @ApiModelProperty("系统用户初始密码")
    @TableField(value = "user_init_password", typeHandler = EncryptHandler.class)
    private String userInitPassword;

}
