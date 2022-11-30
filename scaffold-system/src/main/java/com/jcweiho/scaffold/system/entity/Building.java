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
 * 楼宇信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-11-07
 */
@Getter
@Setter
@ToString
@TableName("building")
@ApiModel(value = "Building对象", description = "楼宇信息表")
public class Building extends CommonEntity {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("建筑栋号")
    @TableField("building_num")
    @NotBlank(message = "建筑栋号不能为空!")
    @Xss
    private String buildingNum;

    @ApiModelProperty("建筑层数")
    @TableField("floor")
    @Min(value = 1, message = "建筑层数必须大于零！")
    private Integer floor;

    @ApiModelProperty("一梯几户")
    @TableField("floor_num")
    @Min(value = 1, message = "户数必须大于零！")
    private Integer floorNum;

    @ApiModelProperty("总户数")
    @TableField("number")
    @Min(value = 1, message = "总户数必须大于零！")
    private Integer number;

}
