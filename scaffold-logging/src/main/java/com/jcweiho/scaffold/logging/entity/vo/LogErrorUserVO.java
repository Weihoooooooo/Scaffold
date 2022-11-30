package com.jcweiho.scaffold.logging.entity.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Weiho
 * @since 2022/9/13
 */
@Data
@ApiModel("LogErrorUserVO对象")
public class LogErrorUserVO {
    @ApiModelProperty("日志主键")
    private Long id;

    @ApiModelProperty("模块名称")
    private String title;

    @ApiModelProperty("操作用户")
    private String username;

    @ApiModelProperty("请求的IP")
    private String requestIp;

    @ApiModelProperty("请求的浏览器")
    private String browser;

    @ApiModelProperty("IP所在地")
    private String address;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
}
