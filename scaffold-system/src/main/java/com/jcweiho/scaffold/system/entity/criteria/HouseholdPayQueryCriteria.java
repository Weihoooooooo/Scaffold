package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2023-01-07
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("业主缴费查询条件")
public class HouseholdPayQueryCriteria extends BaseQueryCriteria {
    @IdDecrypt
    @Query
    @ApiModelProperty("梯户ID")
    private Long householdId;
}
