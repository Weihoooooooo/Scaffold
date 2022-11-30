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
 * @since 2022/11/9
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("楼宇查询条件")
public class BuildingQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "buildingNum")
    @ApiModelProperty("建筑栋号模糊查询！")
    @Xss
    private String blurry;
}
