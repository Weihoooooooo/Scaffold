package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.NoticeToEnum;
import com.jcweiho.scaffold.system.entity.enums.OverdueEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("通知查询条件")
public class NoticeQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "title,content")
    @ApiModelProperty("模糊查询字段")
    @Xss
    private String blurry;

    @Query
    @ApiModelProperty("是否已过期")
    private OverdueEnum isOverdue;

    @Query
    @ApiModelProperty("通知人ID")
    private Long userId;

    @Query
    @ApiModelProperty("通知发送目标范围")
    private NoticeToEnum type;
}
