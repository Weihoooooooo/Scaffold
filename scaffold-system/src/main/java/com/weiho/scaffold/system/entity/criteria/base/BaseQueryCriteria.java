package com.weiho.scaffold.system.entity.criteria.base;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/11/9
 */
@Data
@ToString
@ApiModel("基础查询条件实体")
public class BaseQueryCriteria implements Serializable {

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("创建时间范围")
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> createTime;

}
