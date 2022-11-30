package com.jcweiho.scaffold.logging.entity.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jcweiho.scaffold.logging.enums.BusinessStatusEnum;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Weiho
 * @since 2022/8/29
 */
@Data
@ApiModel(value = "LogErrorVO对象", description = "异常日志VO")
public class LogErrorVO {
    @ApiModelProperty("日志主键")
    private Long id;

    @ApiModelProperty("模块名称")
    private String title;

    @ApiModelProperty("业务类型(0-其他,1-新增,2-修改,3-删除)")
    private BusinessTypeEnum businessType;

    @ApiModelProperty("请求方法名称")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("操作用户")
    private String username;

    @ApiModelProperty("请求URL")
    private String requestUrl;

    @ApiModelProperty("请求的IP")
    private String requestIp;

    @ApiModelProperty("请求的浏览器")
    private String browser;

    @ApiModelProperty("IP所在地")
    private String address;

    @ApiModelProperty("请求参数")
    private String requestParams;

    @ApiModelProperty("响应结果(JSON字符串)")
    private String responseResult;

    @ApiModelProperty("日志级别")
    private String logType;

    @ApiModelProperty("操作状态(0-正常,1-失败)")
    private BusinessStatusEnum status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
}
