package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/9/14
 */
@Data
@ApiModel("用户查询实体")
public class UserQueryCriteria {
    @Query
    @ApiModelProperty("主键")
    private Long id;

    @Query(blurry = "username")
    @ApiModelProperty("模糊查询字段")
    private String blurry;

    @Query(type = QueryTypeEnum.INNER_LIKE)
    @ApiModelProperty("手机号模糊查询")
    private String phone;

    @Query
    @ApiModelProperty("账号是否启用")
    private Boolean enabled;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("注册时间的范围")
    private List<Timestamp> createTime;
}
