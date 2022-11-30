package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.IdEncrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 停车场信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-11-27
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("park_lot")
@ApiModel(value = "ParkLot对象", description = "停车场信息表")
public class ParkLot extends CommonEntity {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("停车场区域")
    @TableField("region")
    @NotBlank(message = "停车场区域不能为空！")
    @Xss
    private String region;

    @ApiModelProperty("停车场小车车位数量")
    @TableField("number")
    @Min(value = 1, message = "车位数至少为1！")
    private Integer number;

    @ApiModelProperty("停车场层数")
    @TableField("floor")
    @Min(value = -3, message = "停车场层数至少为-3！")
    private Integer floor;

    @ApiModelProperty("停车场其他车位数量")
    @TableField("other_number")
    @Min(value = 1, message = "其他车位数量至少为1！")
    private Integer otherNumber;

    @ApiModelProperty("是否启用 0-未启用 1-已启用")
    @TableField("enabled")
    private boolean enabled;

}
