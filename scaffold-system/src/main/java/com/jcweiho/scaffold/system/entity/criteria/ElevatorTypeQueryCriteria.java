package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/11/26
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("电梯类型查询条件")
public class ElevatorTypeQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "name,nameZhCn,nameZhHk,nameZhTw,nameEnUs")
    @ApiModelProperty("模糊查询字段")
    private String blurry;
}
