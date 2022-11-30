package com.jcweiho.scaffold.system.entity.criteria;

import com.jcweiho.scaffold.common.annotation.IdDecrypt;
import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.mp.enums.QueryTypeEnum;
import com.jcweiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/11/12
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel("电梯查询条件")
public class ElevatorQueryCriteria extends BaseQueryCriteria {

    @Query(blurry = "identityId,numberOfPeople,numberOfWeight,hoistwaySize,reservedSizeOfDoorOpening,maintainPeople,maintainPeoplePhone")
    @ApiModelProperty("模糊查询")
    @Xss
    private String blurry;

    @Query
    @IdDecrypt
    @ApiModelProperty("建筑栋号")
    private Long buildingId;

    @Query
    @ApiModelProperty("菜单是否启用")
    private Boolean enabled;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("上一次报修时间范围")
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> lastMaintainTime;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("下一次报修时间范围")
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> nextMaintainTime;
}
