package com.weiho.scaffold.system.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/10/25
 */
@Data
@ToString
@ApiModel("修改业主密码VO")
public class OwnerPassVO implements Serializable {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("原始密码(手机号)")
    private String phone;
}
