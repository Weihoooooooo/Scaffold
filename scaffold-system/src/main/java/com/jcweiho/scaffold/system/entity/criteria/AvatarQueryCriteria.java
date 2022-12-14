package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.AuditEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/9/26
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("头像查询条件")
public class AvatarQueryCriteria extends BaseQueryCriteria {
    @Xss
    @ApiModelProperty("用户名模糊查询")
    private String blurry;

    @ApiModelProperty("头像状态")
    private AuditEnum enabled;
}
