package com.jcweiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jcweiho.scaffold.mp.entity.CommonEntity;
import com.jcweiho.scaffold.system.entity.enums.IsBuyEnum;
import com.jcweiho.scaffold.system.entity.enums.ParkTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 车位信息表信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-12-05
 */
@ToString(callSuper = true)
@Getter
@Setter
@TableName("park")
@ApiModel(value = "Park对象", description = "车位信息表")
public class Park extends CommonEntity {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Long id;

    @TableField("park_lot_id")
    @ApiModelProperty("停车场ID")
    private Long parkLotId;

    @TableField("type")
    @ApiModelProperty("车位类型 0-小车车位 1-其他车位")
    private ParkTypeEnum type;

    @TableField("is_buy")
    @ApiModelProperty("是否被购买 0-未购买 1-已购买")
    private IsBuyEnum isBuy;

    @TableField("identity_id")
    @ApiModelProperty("车位独立编号")
    private String identityId;

    @TableField("price")
    @ApiModelProperty("车位价格")
    private Double price;

}
