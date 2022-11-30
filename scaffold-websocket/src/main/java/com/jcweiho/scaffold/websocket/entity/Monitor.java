package com.jcweiho.scaffold.websocket.entity;

import lombok.Data;

/**
 * 系统监测pojo类
 *
 * @author Weiho
 */
@Data
public class Monitor {
    private String os;//操作系统
    private String runTime;//程序启动时间
    private String jvmJavaVersion;//java版本

    //jvm
    private String jvmHeapInit;//jvm内存的初始大小
    private String jvmHeapMax;//jvm最大可用内存量
    private String jvmHeapUsed;//jvm已使用的内存量
    private String jvmHeapCommitted;//jvm已申请的内存量
    private String jvmNonHeapInit;//jvm内存的初始大小
    private String jvmNonHeapMax;//jvm最大可用内存量
    private String jvmNonHeapUsed;//jvm已使用的内存量
    private String jvmNonHeapCommitted;//jvm已申请的内存量

    //硬件信息
    private String cpuInfo;//CPU信息
    private String cpuUseRate;//CPU使用率
    private String ramTotal;//系统内存总量
    private String ramUsed;//已使用的系统内存量
    private String diskTotal;//系统磁盘总量
    private String diskUsed;//已使用的系统磁盘量
}

