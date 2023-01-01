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
//    @IdEncrypt
//    @IdDecrypt
//    @ApiModelProperty("主键ID")
//    private Long id;
//
//    @IdEncrypt
//    @IdDecrypt
//    @ApiModelProperty("业主ID")
//    private Long ownerId;

    @Desensitize(DesensitizeStrategy.USERNAME)
    @ApiModelProperty("业主姓名")
    @Xss
    private String name;

    @ApiModelProperty("业主手机号")
    @Xss
    private String phone;

//    @IdEncrypt
//    @IdDecrypt
//    @ApiModelProperty("车位ID")
//    private Long parkId;

    @ApiModelProperty("车位信息")
    private ParkVO parkVO;

//    @ApiModelProperty("车牌号码")
//    @NotBlank(message = "车牌号码不能为空！")
//    @Xss
//    private String carNumber;

//    @ApiModelProperty("车辆颜色")
//    @NotBlank(message = "车辆颜色不能为空！")
//    @Xss
//    private String carColor;

    @ApiModelProperty("停车场-停车位信息列表")
    @Size(max = 2, message = "该项大小最大为2！")
    private List<String> parkInfo;
}
