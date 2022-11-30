package com.jcweiho.scaffold.mp.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 通用实体类模板,为字段加入插入时间和修改时间
 *
 * @author Weiho
 */
@Getter
@Setter
public class CommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)//Mybatis-Plus在执行插入时自动注入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)//Mybatis-Plus在执行更新时自动注入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty(value = "是否被删除(逻辑删除)")
    @TableField(value = "is_del")
    @TableLogic(value = "0", delval = "1")//开启逻辑删除,存在 - 0，被删除 - 1
    @JsonIgnore//序列化时忽略该字段 (序列化和反序列化都有影响)
    @JSONField(serialize = false)
    private Integer isDel;
}
