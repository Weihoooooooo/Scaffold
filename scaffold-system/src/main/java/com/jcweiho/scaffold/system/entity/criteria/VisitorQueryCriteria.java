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
 * @since 2023-01-02
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("访客信息查询条件")
public class VisitorQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "name,phone")
    @ApiModelProperty("模糊查询")
    @Xss
    private String blurry;
}
