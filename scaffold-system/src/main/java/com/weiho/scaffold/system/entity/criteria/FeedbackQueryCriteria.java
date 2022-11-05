package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import com.weiho.scaffold.system.entity.enums.FeedbackResultEnum;
import com.weiho.scaffold.system.entity.enums.FeedbackTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Data
@ToString
@ApiModel("反馈查询实体")
public class FeedbackQueryCriteria {

    @Query(blurry = "title,content,remarks")
    @ApiModelProperty("模糊查询")
    private String blurry;

    @Query
    @ApiModelProperty("反馈类型")
    private FeedbackTypeEnum type;

    @Query
    @ApiModelProperty("反馈处理结果")
    private FeedbackResultEnum result;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("创建时间")
    private List<Timestamp> createTime;
}
