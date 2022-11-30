package com.jcweiho.scaffold.logging.entity.criteria;

import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.mp.enums.QueryTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
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
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> createTime;
}
