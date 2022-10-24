package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.common.annotation.Desensitize;
import com.weiho.scaffold.common.sensitive.enums.SensitiveStrategy;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.mp.handler.EncryptHandler;
import com.weiho.scaffold.system.entity.enums.SexEnum;
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
    @TableField(value = "name", typeHandler = EncryptHandler.class)
    @Desensitize(strategy = SensitiveStrategy.USERNAME)
    private String name;

    @ApiModelProperty("业主手机号码")
    @TableField(value = "phone", typeHandler = EncryptHandler.class)
    private String phone;

    @ApiModelProperty("业主身份证号")
    @TableField(value = "identity_id", typeHandler = EncryptHandler.class)
    @Desensitize(strategy = SensitiveStrategy.ID_CARD)
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
