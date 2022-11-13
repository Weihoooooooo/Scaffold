package com.weiho.scaffold.system.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.mp.handler.EncryptHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author Weiho
 * @since 2022/11/1
 */
@ToString
@Data
@ApiModel("系统参数VO")
public class SysSettingVO {
    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("系统名称")
    @TableField("sys_name")
    @NotBlank(message = "系统名称不能为空!")
    private String sysName;

    @ApiModelProperty("系统名称中文")
    @TableField("sys_name_zh_cn")
    private String sysNameZhCn;

    @ApiModelProperty("系统名称中国香港")
    @TableField("sys_name_zh_hk")
    private String sysNameZhHk;

    @ApiModelProperty("系统名称中国台湾")
    @TableField("sys_name_zh_tw")
    private String sysNameZhTw;

    @ApiModelProperty("系统名称英文")
    @TableField("sys_name_en_us")
    private String sysNameEnUs;

    @ApiModelProperty("系统logo")
    @TableField("sys_logo")
    @NotBlank(message = "系统Logo不能为空!")
    @Pattern(regexp = "[a-zA-z]+://[^\\s]*", message = "Logo的地址不正确")
    private String sysLogo;

    @ApiModelProperty("系统用户初始密码")
    @TableField(value = "user_init_password", typeHandler = EncryptHandler.class)
    private String userInitPassword;
}
