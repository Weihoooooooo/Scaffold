package com.weiho.scaffold.logging.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 日志查询条件
 *
 * @author Weiho
 * @since 2022/8/29
 */
@Data
@ApiModel("日志查询条件")
public class LogQueryCriteria {
    @ApiModelProperty("高级模糊查询字段")
    @Query(blurry = "title,method,requestMethod,username,requestUrl,requestIp,browser,address,requestParams,time")
    private String blurry;

    @Query
    @ApiModelProperty(value = "日志的类型(INFO,ERROR)", hidden = true)
    private String logType;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("创建时间的范围(List装)")
    private List<Timestamp> createTime;
}
