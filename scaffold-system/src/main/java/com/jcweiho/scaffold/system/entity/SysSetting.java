package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.mp.handler.EncryptHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

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
    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Xss
    @ApiModelProperty("系统名称")
    @TableField("name")
    @NotBlank(message = "系统名称不能为空!")
    private String name;

    @Xss
    @ApiModelProperty("系统名称中文")
    @TableField("name_zh_cn")
    private String nameZhCn;

    @Xss
    @ApiModelProperty("系统名称中国香港")
    @TableField("name_zh_hk")
    private String nameZhHk;

    @Xss
    @ApiModelProperty("系统名称中国台湾")
    @TableField("name_zh_tw")
    private String nameZhTw;

    @Xss
    @ApiModelProperty("系统名称英文")
    @TableField("name_en_us")
    private String nameEnUs;

    @Xss
    @ApiModelProperty("系统logo")
    @TableField("sys_logo")
    @NotBlank(message = "系统Logo不能为空!")
    @Pattern(regexp = "[a-zA-z]+://[^\\s]*", message = "Logo的地址不正确")
    private String sysLogo;

    @Xss
    @ApiModelProperty("系统用户初始密码")
    @TableField(value = "user_init_password", typeHandler = EncryptHandler.class)
    private String userInitPassword;

}
