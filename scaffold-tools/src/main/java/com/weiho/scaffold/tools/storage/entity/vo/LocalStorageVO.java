package com.weiho.scaffold.tools.storage.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Weiho
 * @since 2022/10/27
 */
@Data
@ToString
@ApiModel("修改本地存储VO")
public class LocalStorageVO implements Serializable {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("文件名")
    @NotBlank(message = "文件名不能为空！")
    private String fileName;
}
