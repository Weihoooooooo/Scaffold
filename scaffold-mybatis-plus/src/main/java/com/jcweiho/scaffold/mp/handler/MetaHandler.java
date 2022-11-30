package com.jcweiho.scaffold.mp.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * Mybatis-Plus字段自动注入配置类,配合CommonEntity使用
 * 自动插入[createTime]和[updateTime]两个字段
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class MetaHandler implements MetaObjectHandler {
    /**
     * 插入时自动插入时间
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());//获取系统时间
            //先判断字段是否有Setter方法
            if (metaObject.hasSetter("createTime")) {
                log.info("Mybatis-Plus -> [{}]", "自动注入 createTime");
                this.setFieldValByName("createTime", timestamp, metaObject);//注入
            }
            if (metaObject.hasSetter("updateTime")) {
                log.info("Mybatis-Plus -> [{}]", "自动注入 updateTime");
                this.setFieldValByName("updateTime", timestamp, metaObject);
            }
        } catch (Exception e) {
            log.error("Mybatis-Plus -> [(insertFill)自动注入失败,错误{}]", e);
        }
    }

    /**
     * 修改时自动插入时间
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (metaObject.hasSetter("updateTime")) {
                log.info("Mybatis-Plus -> [{}]", "自动注入 updateTime");
                this.setFieldValByName("updateTime", timestamp, metaObject);
            }
        } catch (Exception e) {
            log.error("Mybatis-Plus -> [(updateFill)自动注入失败,错误{}]", e);
        }
    }
}
