package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.system.entity.enums.PayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 业主缴费信息表
 * </p>
 *
 * @author Weiho
 * @since 2023-01-07
 */
@ToString(callSuper = true)
@Getter
@Setter
@TableName("household_pay")
@ApiModel(value = "HouseholdPay对象", description = "业主缴费")
public class HouseholdPay extends CommonEntity {
    @IdEncrypt
    @IdDecrypt
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @IdEncrypt
    @IdDecrypt
    @TableField("household_id")
    @ApiModelProperty("梯户ID")
    private Long householdId;

    @TableField("pay_water")
    @ApiModelProperty("水费")
    private Double payWater;

    @TableField("pay_electric")
    @ApiModelProperty("电费")
    private Double payElectric;

    @TableField("year")
    @ApiModelProperty("年份")
    private Long year;

    @TableField("month")
    @ApiModelProperty("月份")
    private Integer month;

    @TableField("pay_property")
    @ApiModelProperty("物管费")
    private Double payProperty;

    @TableField("is_pay_water")
    @ApiModelProperty("是否支付水费")
    private PayEnum isPayWater;

    @TableField("is_pay_electric")
    @ApiModelProperty("是否支付电费")
    private PayEnum isPayElectric;

    @TableField("is_pay_property")
    @ApiModelProperty("是否支付物管费")
    private PayEnum isPayProperty;

}
