package com.weiho.scaffold.system.entity.vo;

import com.weiho.scaffold.system.entity.Elevator;
import com.weiho.scaffold.system.entity.ElevatorType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/11/12
 */
@Getter
@Setter
@ToString
@ApiModel("电梯VO")
public class ElevatorVO extends Elevator implements Serializable {

    @ApiModelProperty("建筑栋号")
    private String buildingNum;

    @ApiModelProperty("电梯类型")
    private Set<ElevatorType> elevatorTypes;

    @ApiModelProperty("电梯类型数组")
    private Set<String> elevatorTypesString;
}
