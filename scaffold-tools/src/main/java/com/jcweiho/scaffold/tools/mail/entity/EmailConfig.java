package com.jcweiho.scaffold.tools.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author Weiho
 * @since 2022-09-05
 */
@Getter
@Setter
@ToString
@TableName("email_config")
@ApiModel(value = "EmailConfig对象")
public class EmailConfig extends CommonEntity {

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发件人邮箱")
    @TableField("from_user")
    private String fromUser;

    @ApiModelProperty("邮件服务器SMTP地址")
    @TableField("host")
    private String host;

    @ApiModelProperty("端口")
    @TableField("port")
    private String port;

    @ApiModelProperty("授权码")
    @TableField("pass")
    private String pass;

    @ApiModelProperty("发送人名称")
    @TableField("username")
    private String username;
}
