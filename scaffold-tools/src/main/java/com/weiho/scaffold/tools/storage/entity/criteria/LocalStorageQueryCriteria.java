package com.weiho.scaffold.tools.storage.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/10/27
 */
@Data
@ToString
@ApiModel("本地存储查询实体")
public class LocalStorageQueryCriteria implements Serializable {
    @Query(blurry = "username,fileName,realName,type,suffix")
    @ApiModelProperty("模糊查询")
    private String blurry;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("注册时间的范围")
    private List<Timestamp> createTime;
}
