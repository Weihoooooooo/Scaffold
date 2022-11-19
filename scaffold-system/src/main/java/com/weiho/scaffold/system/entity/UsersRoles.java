package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * <p>
 * 用户角色中间表
 * </p>
 *
 * @author Weiho
 * @since 2022-09-21
 */
@Alias("usersRoles")
@Getter
@Setter
@ToString
@TableName("users_roles")
@ApiModel(value = "UsersRoles对象", description = "用户角色中间表")
public class UsersRoles implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    @ApiModelProperty("用户主键ID")
    private Long userId;

    @TableField("role_id")
    @ApiModelProperty("角色主键ID")
    private Long roleId;
}
