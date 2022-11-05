package com.weiho.scaffold.system.entity.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiho.scaffold.common.annotation.Desensitize;
import com.weiho.scaffold.common.sensitive.enums.SensitiveStrategy;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.enums.FeedbackResultEnum;
import com.weiho.scaffold.system.entity.enums.FeedbackTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Weiho
 * @since 2022/11/4
 */
@Getter
@Setter
@ToString
@ApiModel(value = "Feedback对象", description = "反馈信息表")
public class FeedbackVO extends CommonEntity implements Serializable {

    @ApiModelProperty("主键ID")
    private Long id;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty("业主ID")
    private Long ownerId;

    @ApiModelProperty("业主姓名")
    @Desensitize(strategy = SensitiveStrategy.USERNAME)
    private String ownerName;

    @ApiModelProperty("处理人")
    private String username;

    @ApiModelProperty("反馈类型 0-报修,1-投诉,2-建议")
    private FeedbackTypeEnum type;

    @ApiModelProperty("反馈标题")
    private String title;

    @ApiModelProperty("反馈的内容")
    private String content;

    @ApiModelProperty("反馈图片")
    private String feedbackImage;

    @ApiModelProperty("反馈回复")
    @NotBlank(message = "回复不能为空！")
    private String answer;

    @ApiModelProperty("回复时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date answerTime;

    @ApiModelProperty("处理结果 0-未解决 1-已解决 2-其他")
    private FeedbackResultEnum result;

    @ApiModelProperty("备注")
    private String remarks;

}
