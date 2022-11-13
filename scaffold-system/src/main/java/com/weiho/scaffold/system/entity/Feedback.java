package com.weiho.scaffold.system.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.enums.FeedbackResultEnum;
import com.weiho.scaffold.system.entity.enums.FeedbackTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 反馈信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-11-04
 */
@Getter
@Setter
@TableName("feedback")
@ApiModel(value = "Feedback对象", description = "反馈信息表")
public class Feedback extends CommonEntity {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("业主ID")
    @TableField("owner_id")
    @JsonIgnore
    @JSONField(serialize = false)
    private Long ownerId;

    @ApiModelProperty("处理人")
    @TableField("username")
    private String username;

    @ApiModelProperty("反馈类型 0-报修,1-投诉,2-建议")
    @TableField("type")
    private FeedbackTypeEnum type;

    @ApiModelProperty("反馈标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("反馈的内容")
    @TableField("content")
    private String content;

    @ApiModelProperty("反馈图片")
    @TableField("feedback_image")
    private String feedbackImage;

    @ApiModelProperty("反馈回复")
    @TableField("answer")
    private String answer;

    @ApiModelProperty("回复时间")
    @TableField("answer_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date answerTime;

    @ApiModelProperty("处理结果 0-未解决 1-已解决 2-其他")
    @TableField("result")
    private FeedbackResultEnum result;

    @ApiModelProperty("备注")
    @TableField("remarks")
    private String remarks;

}
