package com.jcweiho.scaffold.tools.storage.entity.criteria;

import com.jcweiho.scaffold.common.annotation.Xss;
import com.jcweiho.scaffold.mp.annotation.Query;
import com.jcweiho.scaffold.mp.enums.QueryTypeEnum;
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
 * @since 2022/10/27
 */
@Data
@ToString
@ApiModel("本地存储查询实体")
public class LocalStorageQueryCriteria implements Serializable {
    @Query(blurry = "username,fileName,realName,type,suffix")
    @ApiModelProperty("模糊查询")
    @Xss
    private String blurry;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("注册时间的范围")
    @Size(max = 2, message = "时间范围必须是两个！")
    private List<Timestamp> createTime;
}
