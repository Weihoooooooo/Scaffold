package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

/**
 * @author Weiho
 * @since 2022/10/21
 */
@Data
@ToString
@ApiModel("业主查询实体")
public class OwnerQueryCriteria {
    @Query(blurry = "")
    private String blurry;
}
