package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.mp.entity.CommonEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author Weiho
 * @since 2022-11-19
 */
@Getter
@Setter
@TableName("elevator_type")
@ApiModel(value = "ElevatorType对象")
public class ElevatorType extends CommonEntity {

    @ApiModelProperty("电梯类型主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(" 电梯类型")
    @TableField("type")
    private String type;

}
