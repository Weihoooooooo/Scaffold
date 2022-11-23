package com.weiho.scaffold.system.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.enums.ElevatorComputerRoomEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.Date;

/**
 * <p>
 * 电梯信息表
 * </p>
 *
 * @author Weiho
 * @since 2022-11-12
 */
@Getter
@Setter
@ToString
@TableName("elevator")
@ApiModel(value = "Elevator对象", description = "电梯信息表")
public class Elevator extends CommonEntity {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("所属楼宇")
    @TableField(value = "building_id")
    private Long buildingId;

    @ApiModelProperty("电梯独立编号")
    @TableField("identity_id")
    @NotBlank(message = "电梯独立编号不能为空！")
    private String identityId;

    @ApiModelProperty("核载人数")
    @TableField("number_of_people")
    @Min(value = 1, message = "核载人数至少为1！")
    private Integer numberOfPeople;

    @ApiModelProperty("核载重量(kg)")
    @TableField("number_of_weight")
    @DecimalMin(value = "0.01", message = "核载重量至少0.01！")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double numberOfWeight;

    @ApiModelProperty("有无机房(0 - 无机房,1 - 有机房)")
    @TableField("is_computer_room")
    private ElevatorComputerRoomEnum isComputerRoom;

    @ApiModelProperty("井道尺寸(长 * 宽)(mm)")
    @TableField("hoistway_size")
    private String hoistwaySize;

    @ApiModelProperty("基坑深度(m)")
    @TableField("depth_of_foundation_pit")
    @DecimalMin(value = "0.1", message = "基坑深度至少0.01!")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double depthOfFoundationPit;

    @ApiModelProperty("门洞预留尺寸(长 * 宽)(mm)")
    @TableField("reserved_size_of_door_opening")
    private String reservedSizeOfDoorOpening;

    @ApiModelProperty("提升高度(m)")
    @TableField("lifting_height")
    @DecimalMin(value = "0.1", message = "基坑深度至少0.01!")
    @Digits(message = "该项必须为数字！", integer = 8, fraction = 2)
    private Double liftingHeight;

    @ApiModelProperty("上一次维护时间")
    @TableField("last_maintain_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastMaintainTime;

    @ApiModelProperty("下一次维护时间")
    @TableField("next_maintain_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date nextMaintainTime;

    @ApiModelProperty("相隔多少天维护一次")
    @TableField("day")
    @Min(value = 1, message = "维护间隔至少为1！")
    private Integer day;

    @ApiModelProperty("维护人姓名")
    @TableField("maintain_people")
    private String maintainPeople;

    @ApiModelProperty("维护人电话号码")
    @TableField("maintain_people_phone")
    @Pattern(regexp = "^(13\\d|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18\\d|19[0-35-9])\\d{8}$", message = "手机号码格式不正确！")
    private String maintainPeoplePhone;

    @ApiModelProperty("是否启用 0-未启用 1-已启用")
    @TableField("enabled")
    private boolean enabled;

}
