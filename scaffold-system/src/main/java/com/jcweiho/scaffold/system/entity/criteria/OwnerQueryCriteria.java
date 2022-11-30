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
 * @since 2022/10/21
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("业主查询条件")
public class OwnerQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "phone,name")
    @ApiModelProperty("手机号码后四位，姓名模糊查询")
    @Xss
    private String blurry;
}
