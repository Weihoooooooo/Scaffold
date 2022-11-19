package com.weiho.scaffold.mp.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weiho.scaffold.common.util.StringUtils;
import com.weiho.scaffold.mp.annotation.Query;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 根据注解@Query生成Mybatis-Plus条件构造器
 *
 * @author yshopmall - <a href="https://gitee.com/guchengwuyue/yshopmall">参考链接</a>
 */
@Slf4j
@SuppressWarnings({"unchecked", "all"})
public class QueryHelper {
    /**
     * 获取条件构造器
     *
     * @param obj   结果对象
     * @param query 查询条件
     * @param <R>
     * @param <Q>
     * @return
     */
    public static <R, Q> QueryWrapper getQueryWrapper(R obj, Q query) {
        QueryWrapper<R> queryWrapper = new QueryWrapper<R>();
        if (query == null) {
            return queryWrapper;
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                Query q = field.getAnnotation(Query.class);
                if (q != null) {
                    String propName = q.propName();
                    String blurry = q.blurry();
                    String attributeName = StringUtils.isBlank(propName) ? field.getName() : propName;
                    attributeName = StringUtils.toUnderline(attributeName);
                    Class<?> fieldType = field.getType();
                    Object val = field.get(query);
                    if (ObjectUtil.isNull(val) || "".equals(val)) {
                        continue;
                    }
                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(",");
                        queryWrapper.and(wrapper -> {
                            for (int i = 0; i < blurrys.length; i++) {
                                String column = StringUtils.toUnderline(blurrys[i]);
                                wrapper.or().like(column, val.toString());
                            }
                        });
                        continue;
                    }
                    String finalAttributeName = attributeName;
                    switch (q.type()) {
                        case EQUAL:
                            //queryWrapper.and(wrapper -> wrapper.eq(finalAttributeName, val));
                            queryWrapper.eq(attributeName, val);
                            break;
                        case GREATER_THAN:
                            queryWrapper.ge(finalAttributeName, val);
                            break;
                        case LESS_THAN:
                            queryWrapper.le(finalAttributeName, val);
                            break;
                        case LESS_THAN_NQ:
                            queryWrapper.lt(finalAttributeName, val);
                            break;
                        case INNER_LIKE:
                            queryWrapper.like(finalAttributeName, val);
                            break;
                        case LEFT_LIKE:
                            queryWrapper.likeLeft(finalAttributeName, val);
                            break;
                        case RIGHT_LIKE:
                            queryWrapper.likeRight(finalAttributeName, val);
                            break;
                        case IN:
                            if (CollUtil.isNotEmpty((Collection<Long>) val)) {
                                queryWrapper.in(finalAttributeName, (Collection<Long>) val);
                            }
                            break;
                        case NOT_EQUAL:
                            queryWrapper.ne(finalAttributeName, val);
                            break;
                        case NOT_NULL:
                            queryWrapper.isNotNull(finalAttributeName);
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            queryWrapper.between(finalAttributeName, between.get(0), between.get(1));
                            break;
                        case UNIX_TIMESTAMP:
                            List<Object> UNIX_TIMESTAMP = new ArrayList<>((List<Object>) val);
                            if (!UNIX_TIMESTAMP.isEmpty()) {
                                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                long time1 = fm.parse(UNIX_TIMESTAMP.get(0).toString()).getTime() / 1000;
                                long time2 = fm.parse(UNIX_TIMESTAMP.get(1).toString()).getTime() / 1000;
                                queryWrapper.between(finalAttributeName, time1, time2);
                            }
                            break;
                        default:
                            break;
                    }
                }
                field.setAccessible(accessible);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return queryWrapper;
    }

    /**
     * 获取一个类中所有属性的类型和属性名
     *
     * @param clazz  类
     * @param fields 属性类型列表和属性名
     * @return 例如：[java.lang.String com.r6.util.Test.str1, java.lang.String com.r6.util.Test.str2]
     */
    private static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }

}

