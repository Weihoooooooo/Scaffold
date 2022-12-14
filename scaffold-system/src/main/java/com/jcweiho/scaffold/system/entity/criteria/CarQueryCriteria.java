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
 * @since 2022-12-08
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("业主车辆信息表查询条件")
public class CarQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "carNumber,carColor")
    @ApiModelProperty("模糊查询")
    @Xss
    private String blurry;

    @ApiModelProperty("业主姓名模糊查询")
    @Xss
    private String name;
}
