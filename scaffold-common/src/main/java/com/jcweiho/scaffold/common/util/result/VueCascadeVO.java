package com.jcweiho.scaffold.common.util.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Weiho
 * @since 2022/12/11
 */
@Getter
@Setter
@ToString
@ApiModel("级联选择器VO")
public class VueCascadeVO extends VueSelectVO {

    @ApiModelProperty("下级")
    private List<?> children;

    public VueCascadeVO(Object value, String label, List<?> children) {
        super(value, label);
        this.children = children;
    }
}
