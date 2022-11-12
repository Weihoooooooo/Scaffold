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
 * @since 2022/9/14
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("角色查询条件")
public class RoleQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "name,permission,nameZhCn,nameZhTw,nameZhHk,nameEnUs")
    @ApiModelProperty("模糊查询字段")
    private String blurry;
}
