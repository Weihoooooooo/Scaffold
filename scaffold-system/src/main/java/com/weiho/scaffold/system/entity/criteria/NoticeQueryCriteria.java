package com.weiho.scaffold.system.entity.criteria;

import com.weiho.scaffold.mp.annotation.Query;
import com.weiho.scaffold.mp.enums.QueryTypeEnum;
import com.weiho.scaffold.system.entity.enums.NoticeToEnum;
import com.weiho.scaffold.system.entity.enums.OverdueEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@Data
@ToString
@ApiModel("通知查询实体")
public class NoticeQueryCriteria {
    @Query(blurry = "title,content")
    @ApiModelProperty("模糊查询字段")
    private String blurry;

    @Query
    @ApiModelProperty("是否已过期")
    private OverdueEnum isOverdue;

    @Query
    @ApiModelProperty("通知人ID")
    private Long userId;

    @Query
    @ApiModelProperty("通知发送目标范围")
    private NoticeToEnum type;

    @Query(type = QueryTypeEnum.BETWEEN)
    @ApiModelProperty("注册时间的范围")
    private List<Timestamp> createTime;
}
