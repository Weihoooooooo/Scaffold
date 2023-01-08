package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.HouseholdPay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2023-01-07
 */
@Getter
@Setter
@ToString
@ApiModel("业主缴费VO")
public class HouseholdPayVO extends HouseholdPay implements Serializable {
    @ApiModelProperty("梯户信息")
    private Household household;

    @ApiModelProperty("业主姓名")
    private String ownerName;

    @Xss
    @ApiModelProperty("建筑栋号")
    private String buildingNum;
}
