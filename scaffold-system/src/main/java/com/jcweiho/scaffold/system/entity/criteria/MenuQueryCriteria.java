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
 * @since 2022/10/10
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("菜单查询条件")
public class MenuQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "name,nameZhCn,nameZhHk,nameZhTw,nameEnUs,iconCls,permission")
    @ApiModelProperty("用户名模糊查询")
    @Xss
    private String blurry;

    @Query
    @ApiModelProperty("菜单是否启用")
    private Boolean enabled;
}
