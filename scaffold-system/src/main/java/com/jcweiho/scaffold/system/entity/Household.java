package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.system.entity.enums.IsLiveEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 梯户信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-12-13
 */
@ToString(callSuper = true)
@Getter
@Setter
@TableName("household")
@ApiModel(value = "Household对象", description = "梯户信息表")
public class Household extends CommonEntity {
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
    @TableField("building_id")
    @ApiModelProperty("楼宇ID")
    private Long buildingId;

    @Xss
    @NotBlank(message = "梯户独立编号不能为空！")
    @TableField("identity_id")
    @ApiModelProperty("梯户独立编号")
    private String identityId;

    @TableField("area")
    @ApiModelProperty("面积(m^3)")
    @DecimalMin(value = "0.01", message = "面积至少0.01！")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double area;

    @TableField("meter_water")
    @ApiModelProperty("水表读数")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double meterWater;

    @TableField("last_meter_water")
    @ApiModelProperty("上一次水表读数")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double lastMeterWater;

    @TableField("meter_electric")
    @ApiModelProperty("电表读数")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double meterElectric;

    @TableField("last_meter_electric")
    @ApiModelProperty("上一次电表读数")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double lastMeterElectric;

    @TableField("people_number")
    @ApiModelProperty("常住人数")
    @Min(value = 0, message = "常住人数不能小于零")
    private Integer peopleNumber;

    @TableField("is_live")
    @ApiModelProperty("是否居住 0-未居住 1-已居住")
    private IsLiveEnum isLive;

}
