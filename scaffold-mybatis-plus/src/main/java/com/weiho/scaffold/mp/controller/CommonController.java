package com.weiho.scaffold.mp.controller;

import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.CollUtils;
import com.weiho.scaffold.common.util.StringUtils;
import com.weiho.scaffold.common.util.result.Result;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.common.util.secure.SecureType;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.service.CommonService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

/**
 * @author Weiho
 * @since 2022/11/19
 */
@Getter
@SuppressWarnings("all")
public abstract class CommonController<S extends CommonService<E>, E> {
    @Autowired
    protected S baseService;

    @Getter
    @RequiredArgsConstructor
    public enum Operate {
        ADD("add"),
        UPDATE("update"),
        DELETE("delete"),
        OPERATE("operate");

        private final String operate;
    }

    private enum DecryptType {
        DES,
        AES
    }

    /**
     * 通用修改返回模板方法
     *
     * @param operateType 业务操作
     * @param operate     修改操作
     * @return Result实体
     */
    protected static Result resultMessage(Operate operateType, boolean operate) {
        return operate ?
                Result.success(I18nMessagesUtils.get(operateType.getOperate() + ".success.tip"))
                : Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get(operateType.getOperate() + ".fail.tip"));
    }

    /**
     * 集合空值检查
     *
     * @param ids          传入的集合
     * @param errorMessage 异常的信息
     * @return 处理后的数组
     */
    protected static Set<String> filterNull(Set<String> ids, String errorMessage) {
        // 非空判断
        if (CollUtils.isEmpty(ids)) {
            throw new BadRequestException(errorMessage);
        }

        // 去除null元素
        return CollUtils.removeNull(ids);
    }

    /**
     * 集合空值检查
     *
     * @param id           传入的字段
     * @param errorMessage 异常的信息
     * @return 处理后的数组
     */
    protected static String filterNull(String id, String errorMessage) {
        // 非空判断
        if (StringUtils.isEmpty(id)) {
            throw new BadRequestException(errorMessage);
        }

        // 去除null元素
        return StringUtils.trim(id);
    }

    /**
     * 集合空值检查
     *
     * @param ids          传入的集合
     * @param errorMessage 异常的信息
     * @return 处理后的数组
     */
    protected static void filterMapNull(Map<?, ?> ids, String errorMessage) {
        if (CollUtils.isEmpty(ids)) {
            throw new BadRequestException(errorMessage);
        }
    }

    /**
     * 集合去空并将加密的字符串主键解密成Long集合
     *
     * @param ids 加密的主键集合
     * @return 明文主键集合
     */
    protected static Set<Long> filterCollNullAndDecrypt(Set<String> ids) {
        return filterCollNullAndDecrypt(ids, I18nMessagesUtils.get("common.controller.data.null.tip"));
    }

    /**
     * 集合去空并将加密的字符串主键解密成Long集合
     *
     * @param ids          加密的主键集合
     * @param errorMessage 错误消息
     * @return 明文主键集合
     */
    protected static Set<Long> filterCollNullAndDecrypt(Set<String> ids, String errorMessage) {
        return filterCollNullAndDecrypt(ids, DecryptType.DES, errorMessage);
    }

    /**
     * 集合去空并将加密的字符串主键解密成Long集合
     *
     * @param ids          加密的主键集合
     * @param type         解密的方式
     * @param errorMessage 错误消息
     * @return 明文主键集合
     */
    protected static Set<Long> filterCollNullAndDecrypt(Set<String> ids, DecryptType type, String errorMessage) {
        // 集合空值处理
        ids = filterNull(ids, errorMessage);

        SecureType secureType;

        // 开始解密 (首先判断加密方式)
        if (type == DecryptType.AES) {
            secureType = IdSecureUtils.aes();
        } else {
            secureType = IdSecureUtils.des();
        }

        return secureType.decrypt(ids);
    }

    /**
     * 去空并将加密的字符串主键解密成Long
     *
     * @param id           加密的主键
     * @param errorMessage 错误消息
     * @return 明文主键
     */
    protected static Long filterNullAndDecrypt(String id) {
        return filterNullAndDecrypt(id, DecryptType.DES, I18nMessagesUtils.get("common.controller.data.null.tip"));
    }

    /**
     * 去空并将加密的字符串主键解密成Long
     *
     * @param id           加密的主键
     * @param errorMessage 错误消息
     * @return 明文主键
     */
    protected static Long filterNullAndDecrypt(String id, String errorMessage) {
        return filterNullAndDecrypt(id, DecryptType.DES, errorMessage);
    }

    /**
     * 去空并将加密的字符串主键解密成Long
     *
     * @param id           加密的主键
     * @param type         解密的方式
     * @param errorMessage 错误消息
     * @return 明文主键
     */
    protected static Long filterNullAndDecrypt(String id, DecryptType type, String errorMessage) {
        // 集合空值处理
        id = filterNull(id, errorMessage);

        SecureType secureType;

        // 开始解密 (首先判断加密方式)
        if (type == DecryptType.AES) {
            secureType = IdSecureUtils.aes();
        } else {
            secureType = IdSecureUtils.des();
        }

        return secureType.decrypt(id);
    }
}
