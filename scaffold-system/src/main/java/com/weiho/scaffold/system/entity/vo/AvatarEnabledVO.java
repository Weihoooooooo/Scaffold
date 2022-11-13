package com.weiho.scaffold.system.entity.vo;

import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.system.entity.enums.AuditEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/9/27
 */
@Data
@ToString
@ApiModel("修改用户头像状态VO")
public class AvatarEnabledVO implements Serializable {

    @IdDecrypt
    @ApiModelProperty("头像主键")
    private Long id;

    @ApiModelProperty("头像状态")
    private AuditEnum enabled;
}
