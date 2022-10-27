package com.weiho.scaffold.tools.storage.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Weiho
 * @since 2022-10-27
 */
@Getter
@Setter
@ToString
@TableName("local_storage")
@ApiModel(value = "LocalStorage对象")
public class LocalStorage {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户名称")
    @TableField("username")
    private String username;

    @ApiModelProperty("文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("文件真实名")
    @TableField("real_name")
    private String realName;

    @ApiModelProperty("文件类别")
    @TableField("type")
    private String type;

    @ApiModelProperty("文件的大小")
    @TableField("size")
    private String size;

    @ApiModelProperty("文件后缀")
    @TableField("suffix")
    private String suffix;

    @ApiModelProperty("文件服务器地址")
    @TableField("server_url")
    private String serverUrl;

    @ApiModelProperty("文件本地地址")
    @TableField("local_url")
    private String localUrl;

    @ApiModelProperty("文件的MD5值")
    @TableField("md5_code")
    private String md5Code;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
