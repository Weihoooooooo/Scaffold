package com.weiho.scaffold.common.sensitive.convert;

import cn.hutool.core.lang.Assert;
import com.weiho.scaffold.common.annotation.EnumConvertMethod;
import com.weiho.scaffold.common.util.CollUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 工厂类，用于创建 EnumMvcConverter
 * MVC对Enum枚举类的映射
 *
 * @author Weiho
 * @since 2022/11/20
 */
@SuppressWarnings("all")
public class EnumMvcConverter implements ConverterFactory<String, Enum<?>> {
    private final ConcurrentMap<Class<? extends Enum<?>>, EnumMvcConverterHolder> holderMapper = new ConcurrentHashMap<>();

    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(Class<T> targetType) {
        EnumMvcConverterHolder holder = holderMapper.computeIfAbsent(targetType, EnumMvcConverterHolder::createHolder);
        return (Converter<String, T>) holder.converter;
    }

    @AllArgsConstructor
    static class EnumMvcConverterHolder {
        final EnumMvcConvert<?> converter;

        static EnumMvcConverterHolder createHolder(Class<?> targetType) {
            List<Method> methodList = MethodUtils.getMethodsListWithAnnotation(targetType, EnumConvertMethod.class, false, true);
            if (CollUtils.isEmpty(methodList)) {
                return new EnumMvcConverterHolder(null);
            }
            Assert.isTrue(methodList.size() == 1, "@EnumConvertMethod 只能标记在一个工厂方法(静态方法)上");
            Method method = methodList.get(0);
            Assert.isTrue(Modifier.isStatic(method.getModifiers()), "@EnumConvertMethod 只能标记在工厂方法(静态方法)上");
            return new EnumMvcConverterHolder(new EnumMvcConvert<>(method));
        }

    }

    /**
     * 自定义枚举转换器，完成自定义数字属性到枚举类的转化
     *
     * @param <T>
     */
    static class EnumMvcConvert<T extends Enum<T>> implements Converter<String, T> {

        private final Method method;

        public EnumMvcConvert(Method method) {
            this.method = method;
            this.method.setAccessible(true);
        }

        @Override
        public T convert(String source) {
            if (source.isEmpty()) {
                // reset the enum value to null.
                return null;
            }
            try {
                return (T) method.invoke(null, Integer.valueOf(source));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

    }

}
