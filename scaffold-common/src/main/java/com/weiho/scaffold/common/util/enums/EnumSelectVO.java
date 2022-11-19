package com.weiho.scaffold.common.util.enums;

import com.weiho.scaffold.common.util.result.VueSelectVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/9/7
 */
@Getter
@Setter
@ToString
@ApiModel("枚举类选择器VO")
public class EnumSelectVO extends VueSelectVO {

    @ApiModelProperty("枚举类的name")
    private String name;

    public EnumSelectVO(Integer value, String label, String name) {
        super(value, label);
        this.name = name;
    }
}
