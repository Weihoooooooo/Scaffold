package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022-12-13
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("梯户信息表查询条件")
public class HouseholdQueryCriteria extends BaseQueryCriteria {
    @Xss
    @Query(blurry = "identityId,area,peopleNumber")
    @ApiModelProperty("模糊查询")
    private String blurry;
}
