package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.system.entity.enums.IsBuyEnum;
import com.jcweiho.scaffold.system.entity.enums.ParkTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022-12-05
 */
@Getter
@Setter
@ToString
@ApiModel("车位信息表VO")
public class ParkVO extends CommonEntity implements Serializable {
    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @IdDecrypt
    @IdEncrypt
    @ApiModelProperty("停车场ID")
    private Long parkLotId;

    @ApiModelProperty("停车场区域")
    private String region;

    @ApiModelProperty("车位类型 0-小车车位 1-其他车位")
    private ParkTypeEnum type;

    @ApiModelProperty("是否被购买 0-未购买 1-已购买")
    private IsBuyEnum isBuy;

    @ApiModelProperty("车位独立编号")
    @NotBlank(message = "车位独立编号不能为空！")
    private String identityId;

    @ApiModelProperty("车位价格")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double price;

}
