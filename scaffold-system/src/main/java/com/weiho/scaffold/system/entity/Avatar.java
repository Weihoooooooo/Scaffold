package com.weiho.scaffold.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiho.scaffold.common.annotation.IdDecrypt;
import com.weiho.scaffold.common.annotation.IdEncrypt;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.system.entity.enums.AuditEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 用户头像表
 * </p>
 *
 * @author Weiho
 * @since 2022-07-29
 */
@Getter
@Setter
@ToString
@TableName(autoResultMap = true, value = "avatar")
@ApiModel(value = "Avatar对象", description = "用户头像表")
public class Avatar extends CommonEntity {

    @IdEncrypt
    @IdDecrypt
    @ApiModelProperty("主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("真实文件名")
    @TableField(value = "real_name")
    private String realName;

    @ApiModelProperty("文件路径")
    @TableField("path")
    private String path;

    @ApiModelProperty("文件大小")
    @TableField("size")
    private String size;

    @ApiModelProperty("是否审核 1-审核通过 0-审核不通过")
    @TableField("enabled")
    private AuditEnum enabled;
}
