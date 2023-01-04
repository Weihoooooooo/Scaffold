package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 访客信息信息表
 * </p>
 *
 * @author Weiho
 * @since 2023-01-02
 */
@ToString(callSuper = true)
@Getter
@Setter
@TableName("visitor")
@ApiModel(value = "Visitor对象", description = "访客信息")
public class Visitor extends CommonEntity {
    @IdEncrypt
    @IdDecrypt
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @Xss
    @NotBlank(message = "访客姓名不能为空！")
    @TableField("name")
    @ApiModelProperty("访客姓名")
    private String name;

    @Xss
    @NotBlank(message = "访客手机不能为空！")
    @TableField("phone")
    @ApiModelProperty("访客电话号码")
    @Pattern(regexp = "^(13\\d|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18\\d|19[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String phone;

    @IdEncrypt
    @IdDecrypt
    @TableField("building_id")
    @ApiModelProperty("到访楼宇ID")
    private Long buildingId;

    @IdEncrypt
    @IdDecrypt
    @TableField("household_id")
    @ApiModelProperty("到访梯户ID")
    private Long householdId;
}
