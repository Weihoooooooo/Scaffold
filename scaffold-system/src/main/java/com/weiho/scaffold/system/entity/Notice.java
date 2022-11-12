package com.weiho.scaffold.system.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.enums.NoticeToEnum;
import com.weiho.scaffold.system.entity.enums.OverdueEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 通知信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-11-02
 */
@Getter
@Setter
@ToString
@TableName("notice")
@ApiModel(value = "Notice对象", description = "通知信息表")
public class Notice extends CommonEntity {

    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("通知类型 0-全体业主 1-全体员工 2-全体人员")
    @TableField("type")
    private NoticeToEnum type;

    @ApiModelProperty("通知标题")
    @TableField("title")
    @NotBlank(message = "通知标题不能为空！")
    private String title;

    @ApiModelProperty("通知内容")
    @TableField("content")
    @NotBlank(message = "通知内容不能为空！")
    private String content;

    @ApiModelProperty("通知人ID")
    @TableField("user_id")
    @JsonIgnore
    @JSONField(serialize = false)
    private Long userId;

    @ApiModelProperty("是否过期 0-未过期 1-已过期")
    @TableField("is_overdue")
    private OverdueEnum isOverdue;

    @ApiModelProperty("修改人")
    @TableField("update_username")
    private String updateUsername;
}
