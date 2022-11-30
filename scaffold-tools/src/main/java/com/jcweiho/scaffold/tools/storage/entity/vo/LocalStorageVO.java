package com.jcweiho.scaffold.tools.storage.entity.vo;

import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
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

    @IdDecrypt
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("文件名")
    @NotBlank(message = "文件名不能为空！")
    @Xss
    private String fileName;
}
