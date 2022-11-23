package com.weiho.scaffold.system.entity.vo;

import com.weiho.scaffold.common.annotation.Desensitize;
import com.weiho.scaffold.common.sensitive.enums.DesensitizeStrategy;
import com.weiho.scaffold.system.entity.Feedback;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel(value = "反馈VO")
public class FeedbackVO extends Feedback implements Serializable {

    @ApiModelProperty("业主姓名")
    @Desensitize(DesensitizeStrategy.USERNAME)
    private String ownerName;

}
