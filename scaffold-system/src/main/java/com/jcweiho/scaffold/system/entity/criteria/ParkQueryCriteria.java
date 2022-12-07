package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.ParkTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022-12-05
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("车位信息表查询条件")
public class ParkQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "identityId")
    @ApiModelProperty("模糊查询")
    @Xss
    private String blurry;

    @ApiModelProperty("车位类型")
    @Query
    private ParkTypeEnum type;
}
