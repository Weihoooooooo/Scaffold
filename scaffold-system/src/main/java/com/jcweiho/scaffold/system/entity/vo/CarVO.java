package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.Desensitize;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.common.sensitive.enums.DesensitizeStrategy;
import com.jcweiho.scaffold.system.entity.Car;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author Weiho
 * @since 2022-12-08
 */
@Getter
@Setter
@ToString
@ApiModel("业主车辆信息表VO")
public class CarVO extends Car implements Serializable {

    @Desensitize(DesensitizeStrategy.USERNAME)
    @ApiModelProperty("业主姓名")
    @Xss
    private String name;

    @ApiModelProperty("业主手机号")
    @Xss
    private String phone;

    @ApiModelProperty("车位信息")
    private ParkVO parkVO;

    @ApiModelProperty("停车场-停车位信息列表")
    @Size(max = 2, message = "该项大小最大为2！")
    private List<String> parkInfo;
}
