package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/10/21
 */
@Data
@ToString
@ApiModel("业主查询实体")
public class OwnerQueryCriteria {
    @Query(blurry = "phone,name")
    @ApiModelProperty("手机号码后四位，姓名模糊查询")
    private String blurry;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty(value = "注册时间的范围", dataType = "String[]")
    private List<Timestamp> createTime;
}
