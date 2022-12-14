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
 * @since 2022/11/27
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("停车场查询条件")
public class ParkLotQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "region,number,floor,otherNumber")
    @ApiModelProperty("模糊查询字段")
    @Xss
    private String blurry;

    @Query
    @ApiModelProperty("停车场是否启用")
    private Boolean enabled;
}
