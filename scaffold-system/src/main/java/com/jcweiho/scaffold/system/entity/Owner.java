package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.mp.handler.EncryptHandler;
import com.jcweiho.scaffold.mp.handler.NameHandler;
import com.jcweiho.scaffold.mp.handler.PhoneHandler;
import com.jcweiho.scaffold.system.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 业主表
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
@Getter
@Setter
@ToString
@TableName(value = "owner", autoResultMap = true)
@ApiModel(value = "Owner对象", description = "业主表")
public class Owner extends CommonEntity {
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("业主姓名")
    @TableField(value = "name", typeHandler = NameHandler.class)
    private String name;

    @ApiModelProperty("业主手机号码")
    @TableField(value = "phone", typeHandler = PhoneHandler.class)
    private String phone;

    @ApiModelProperty("业主身份证号")
    @TableField(value = "identity_id", typeHandler = EncryptHandler.class)
    private String identityId;

    @ApiModelProperty("业主邮箱")
    @TableField(value = "email", typeHandler = EncryptHandler.class)
    private String email;

    @ApiModelProperty("业主密码")
    @TableField(value = "password", typeHandler = EncryptHandler.class)
    private String password;

    @ApiModelProperty("性别 0-女 1-男")
    @TableField("sex")
    private SexEnum sex;
}
