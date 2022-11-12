package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import com.weiho.scaffold.system.entity.criteria.base.BaseQueryCriteria;
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

    @Query(blurry = "identityId,numberOfPeople,numberOfWeight,maintainPeople,maintainPeoplePhone")
    private String blurry;

    @Query
    @ApiModelProperty("菜单是否启用")
    private Boolean enabled;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("创建时间范围")
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> lastMaintainTime;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("创建时间范围")
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> nextMaintainTime;
}
