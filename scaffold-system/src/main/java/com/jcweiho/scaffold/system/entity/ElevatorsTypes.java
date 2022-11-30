package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@TableName("elevators_types")
@ApiModel(value = "ElevatorsTypes对象")
public class ElevatorsTypes {

    @ApiModelProperty("电梯主键")
    @TableId(value = "elevator_id", type = IdType.AUTO)
    private Long elevatorId;

    @ApiModelProperty("电梯类型主键")
    @TableField(value = "type_id")
    private Long typeId;

}
