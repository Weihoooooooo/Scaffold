package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.Desensitize;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.common.sensitive.enums.DesensitizeStrategy;
import com.jcweiho.scaffold.system.entity.Household;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022-12-13
 */
@Getter
@Setter
@ToString
@ApiModel("梯户信息表VO")
public class HouseholdVO extends Household implements Serializable {

    @Desensitize(DesensitizeStrategy.USERNAME)
    @ApiModelProperty("业主姓名")
    @Xss
    private String ownerName;

    @ApiModelProperty("业主手机号")
    @Xss
    private String phone;

    @Xss
    @ApiModelProperty("建筑栋号")
    private String buildingNum;

}
