package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/10/21
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("业主查询实体")
public class OwnerQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "phone,name")
    @ApiModelProperty("手机号码后四位，姓名模糊查询")
    private String blurry;
}
