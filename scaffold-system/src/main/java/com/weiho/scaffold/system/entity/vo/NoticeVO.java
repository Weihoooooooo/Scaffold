package com.weiho.scaffold.system.entity.vo;

import com.alibaba.fastjson2.annotation.JSONField;
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
import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Setter
@Getter
@ToString
@ApiModel("通知前端显示VO")
public class NoticeVO extends CommonEntity implements Serializable {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("通知类型 0-全体业主 1-全体员工 2-全体人员")
    private NoticeToEnum type;

    @ApiModelProperty("通知标题")
    @NotBlank(message = "通知标题不能为空！")
    private String title;

    @ApiModelProperty("通知内容")
    @NotBlank(message = "通知内容不能为空！")
    private String content;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty("通知人ID")
    private Long userId;

    @ApiModelProperty("通知人")
    private String username;

    @ApiModelProperty("是否过期 0-未过期 1-已过期")
    private OverdueEnum isOverdue;
}
