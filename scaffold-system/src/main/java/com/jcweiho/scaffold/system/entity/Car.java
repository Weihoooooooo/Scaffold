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

/**
 * <p>
 * 业主车辆信息
 * </p>
 *
 * @author Weiho
 * @since 2022-12-08
 */
@ToString(callSuper = true)
@Getter
@Setter
@TableName("car")
@ApiModel(value = "Car对象", description = "业主车辆信息表")
public class Car extends CommonEntity {
    @IdEncrypt
    @IdDecrypt
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @IdEncrypt
    @IdDecrypt
    @TableField("owner_id")
    @ApiModelProperty("业主ID")
    private Long ownerId;

    @IdEncrypt
    @IdDecrypt
    @TableField("park_id")
    @ApiModelProperty("车位ID")
    private Long parkId;

    @Xss
    @TableField("car_number")
    @ApiModelProperty("车牌号码")
    private String carNumber;

    @Xss
    @TableField("car_color")
    @ApiModelProperty("车辆颜色")
    private String carColor;
}
