package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

/**
 * <p>
 * 小区基本信息参数
 * </p>
 *
 * @author Weiho
 * @since 2023-01-04
 */
@Getter
@Setter
@ToString
@TableName("village")
@ApiModel(value = "Village对象", description = "小区基本信息参数")
public class Village extends CommonEntity {

    @IdDecrypt
    @IdEncrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("每平方米物业费")
    @TableField("property")
    @DecimalMin(value = "0.01", message = "每平方米物业费至少0.01！")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double property;

    @ApiModelProperty("每立方米水费")
    @TableField("water")
    @DecimalMin(value = "0.01", message = "每立方米水费至少0.01！")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double water;

    @ApiModelProperty("每度电电费")
    @TableField("electric")
    @DecimalMin(value = "0.01", message = "每度电电费至少0.01！")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double electric;

    @ApiModelProperty("每小时停车费")
    @TableField("park")
    @DecimalMin(value = "0.01", message = "每小时停车费至少0.01！")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double park;

}
