package com.weiho.scaffold.system.entity.vo;

import com.weiho.scaffold.common.annotation.IdDecrypt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Weiho
 * @since 2022/11/16
 */
@Getter
@Setter
@ToString
@ApiModel("电梯维护VO")
public class ElevatorMaintainVO implements Serializable {

    @IdDecrypt
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("上一次维护时间")
    private Date lastMaintainTime;

    @ApiModelProperty("下一次维护时间")
    private Date nextMaintainTime;

    @ApiModelProperty("维护人姓名")
    @NotBlank(message = "维护人姓名不能为空！")
    private String maintainPeople;

    @ApiModelProperty("维护人电话号码")
    @Pattern(regexp = "^(13\\d|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18\\d|19[0-35-9])\\d{8}$", message = "手机号码格式不正确！")
    private String maintainPeoplePhone;
}
