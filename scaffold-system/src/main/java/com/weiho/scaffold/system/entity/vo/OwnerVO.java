package com.weiho.scaffold.system.entity.vo;

import com.weiho.scaffold.common.annotation.Desensitize;
import com.weiho.scaffold.common.sensitive.enums.SensitiveStrategy;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.enums.SexEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/10/21
 */
@Getter
@Setter
@ToString
@ApiModel("业主前端显示实体")
public class OwnerVO extends CommonEntity implements Serializable {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("业主姓名")
    @Desensitize(strategy = SensitiveStrategy.USERNAME)
    private String name;

    @ApiModelProperty("业主手机号码")
    private String phone;

    @ApiModelProperty("业主身份证号")
    @Desensitize(strategy = SensitiveStrategy.ID_CARD)
    private String identityId;

    @ApiModelProperty("业主邮箱")
    private String email;

    @ApiModelProperty("性别 0-女 1-男")
    private SexEnum sex;
}
