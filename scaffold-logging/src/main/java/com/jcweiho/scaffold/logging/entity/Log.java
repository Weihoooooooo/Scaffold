package com.jcweiho.scaffold.logging.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.logging.enums.BusinessStatusEnum;
import com.jcweiho.scaffold.logging.enums.BusinessTypeEnum;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author Weiho
 * @since 2022-08-06
 */
@Getter
@Setter
@TableName("log")
@ToString
@ApiModel(value = "Log对象", description = "操作日志表")
public class Log extends CommonEntity {

    @ApiModelProperty("日志主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("模块名称")
    @TableField("title")
    private String title;

    @ApiModelProperty("业务类型(0-其他,1-新增,2-修改,3-删除)")
    @TableField("business_type")
    private BusinessTypeEnum businessType;

    @ApiModelProperty("请求方法名称")
    @TableField("method")
    private String method;

    @ApiModelProperty("请求方式")
    @TableField("request_method")
    private String requestMethod;

    @ApiModelProperty("操作用户")
    @TableField("username")
    private String username;

    @ApiModelProperty("请求URL")
    @TableField("request_url")
    private String requestUrl;

    @ApiModelProperty("请求的IP")
    @TableField("request_ip")
    private String requestIp;

    @ApiModelProperty("请求的浏览器")
    @TableField("browser")
    private String browser;

    @ApiModelProperty("IP所在地")
    @TableField("address")
    private String address;

    @ApiModelProperty("请求参数")
    @TableField("request_params")
    private String requestParams;

    @ApiModelProperty("响应结果(JSON字符串)")
    @TableField("response_result")
    private String responseResult;

    @ApiModelProperty("日志级别")
    @TableField("log_type")
    private String logType;

    @ApiModelProperty("操作状态(0-正常,1-失败)")
    @TableField("status")
    private BusinessStatusEnum status;

    @ApiModelProperty("错误信息")
    @TableField("exception_detail")
    private String exceptionDetail;

    @ApiModelProperty("消耗时间(ms)")
    @TableField("time")
    private Long time;
}
