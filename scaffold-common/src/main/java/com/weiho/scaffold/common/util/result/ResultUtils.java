package com.weiho.scaffold.common.util.result;

import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.result.enums.ResultCodeEnum;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * @author Weiho
 * @since 2022/11/2
 */
@UtilityClass
public class ResultUtils {
    /**
     * 修改信息的时候返回
     *
     * @param flag 布尔
     * @return /
     */
    public Result updateMessage(boolean flag) {
        if (flag) {
            return Result.success(I18nMessagesUtils.get("update.success.tip"));
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("update.fail.tip"));
        }
    }

    /**
     * 操作的时候返回
     *
     * @param flag 布尔
     * @return /
     */
    public Result operateMessage(boolean flag) {
        if (flag) {
            return Result.success(I18nMessagesUtils.get("operate.success.tip"));
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("operate.fail.tip"));
        }
    }

    /**
     * 添加信息的时候返回
     *
     * @param flag 布尔
     * @return /
     */
    public Result addMessage(boolean flag) {
        if (flag) {
            return Result.success(I18nMessagesUtils.get("add.success.tip"));
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("add.fail.tip"));
        }
    }

    /**
     * 删除信息的时候返回
     *
     * @param ids  传入的主键集合(非空判断)
     * @param flag 布尔
     * @return /
     */
    public Result deleteMessage(Set<Long> ids, boolean flag) {
        if (ids != null && ids.size() > 0) {
            if (flag) {
                return Result.success(I18nMessagesUtils.get("delete.success.tip"));
            } else {
                return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("delete.fail.tip"));
            }
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("delete.fail.tip"));
        }
    }

    /**
     * 删除信息的时候返回
     *
     * @param flag 布尔
     * @return /
     */
    public Result deleteMessage(boolean flag) {
        if (flag) {
            return Result.success(I18nMessagesUtils.get("delete.success.tip"));
        } else {
            return Result.of(ResultCodeEnum.BAD_REQUEST_ERROR, I18nMessagesUtils.get("delete.fail.tip"));
        }
    }
}
