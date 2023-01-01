package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.system.entity.Park;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022-12-05
 */
@Getter
@Setter
@ToString
@ApiModel("车位信息表VO")
public class ParkVO extends Park implements Serializable {

    @Xss
    @ApiModelProperty("停车场区域")
    private String region;

}
