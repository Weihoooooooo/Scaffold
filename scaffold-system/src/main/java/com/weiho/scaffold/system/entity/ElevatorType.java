package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.i18n.I18n;
import com.weiho.scaffold.mp.entity.CommonEntity;
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
@TableName("elevator_type")
@ApiModel(value = "ElevatorType对象")
public class ElevatorType extends CommonEntity implements I18n {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("电梯类型主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("电梯类型")
    @TableField("name")
    private String name;

    @ApiModelProperty("电梯类型zh-CN")
    @TableField("name_zh_cn")
    private String nameZhCn;

    @ApiModelProperty("电梯类型zh-HK")
    @TableField("name_zh_hk")
    private String nameZhHk;

    @ApiModelProperty("电梯类型zh-TW")
    @TableField("name_zh_tw")
    private String nameZhTw;

    @ApiModelProperty("电梯类型en-US")
    @TableField("name_en_us")
    private String nameEnUs;
}
