package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.system.entity.Visitor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2023-01-02
 */
@Getter
@Setter
@ToString
@ApiModel("访客信息VO")
public class VisitorVO extends Visitor implements Serializable {
    @Xss
    @ApiModelProperty("建筑栋号")
    private String buildingNum;

    @ApiModelProperty("梯户信息")
    private HouseholdVO householdVO;
}
