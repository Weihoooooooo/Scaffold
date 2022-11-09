package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import com.weiho.scaffold.system.entity.enums.NoticeToEnum;
import com.weiho.scaffold.system.entity.enums.OverdueEnum;
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
@ApiModel("通知查询实体")
public class NoticeQueryCriteria extends BaseQueryCriteria {
    @Query(blurry = "title,content")
    @ApiModelProperty("模糊查询字段")
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
