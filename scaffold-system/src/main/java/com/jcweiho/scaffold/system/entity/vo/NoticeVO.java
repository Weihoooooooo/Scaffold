package com.jcweiho.scaffold.system.entity.vo;

import com.jcweiho.scaffold.system.entity.Notice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Setter
@Getter
@ToString
@ApiModel("通知VO")
public class NoticeVO extends Notice implements Serializable {

    @ApiModelProperty("通知人")
    private String username;

}
