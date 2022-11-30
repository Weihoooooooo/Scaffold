package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import com.jcweiho.scaffold.system.entity.enums.FeedbackResultEnum;
import com.jcweiho.scaffold.system.entity.enums.FeedbackTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("反馈查询条件")
public class FeedbackQueryCriteria extends BaseQueryCriteria implements Serializable {

    @Query(blurry = "title,content,remarks")
    @ApiModelProperty("模糊查询")
    @Xss
    private String blurry;

    @Query
    @ApiModelProperty("反馈类型")
    private FeedbackTypeEnum type;

    @Query
    @ApiModelProperty("反馈处理结果")
    private FeedbackResultEnum result;
}
