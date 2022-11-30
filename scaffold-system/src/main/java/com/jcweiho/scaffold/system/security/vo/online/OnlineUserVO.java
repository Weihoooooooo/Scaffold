package com.jcweiho.scaffold.system.security.vo.online;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在线用户实体
 *
 * @author Weiho
 * @since 2022/7/29
 */
@Data
@ApiModel("在线用户实体")
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserVO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("IP地址")
    private String ip;

    @ApiModelProperty("IP地址所在城市")
    private String address;

    @ApiModelProperty("Redis中的key")
    private String key;

    @ApiModelProperty("登录的时间")
    private String loginTime;
}
