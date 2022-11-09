package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import com.weiho.scaffold.system.entity.enums.FeedbackResultEnum;
import com.weiho.scaffold.system.entity.enums.FeedbackTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("反馈查询实体")
public class FeedbackQueryCriteria extends BaseQueryCriteria {

    @Query(blurry = "title,content,remarks")
    @ApiModelProperty("模糊查询")
    private String blurry;

    @Query
    @ApiModelProperty("反馈类型")
    private FeedbackTypeEnum type;

    @Query
    @ApiModelProperty("反馈处理结果")
    private FeedbackResultEnum result;
}
