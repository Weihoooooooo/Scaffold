package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.mp.enums.QueryTypeEnum;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
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
@ApiModel("用户查询条件")
public class UserQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "username")
    @ApiModelProperty("模糊查询字段")
    @Xss
    private String blurry;

    @Query(type = QueryTypeEnum.INNER_LIKE)
    @ApiModelProperty("手机号模糊查询")
    @Xss
    private String phone;

    @Query
    @ApiModelProperty("账号是否启用")
    private Boolean enabled;
}
