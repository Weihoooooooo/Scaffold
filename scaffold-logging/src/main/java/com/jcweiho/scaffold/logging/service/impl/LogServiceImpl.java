package com.jcweiho.scaffold.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.logging.annotation.Logging;
import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.logging.entity.convert.LogErrorUserVOConvert;
import com.jcweiho.scaffold.logging.entity.convert.LogErrorVOConvert;
import com.jcweiho.scaffold.logging.entity.convert.LogUserVOConvert;
import com.jcweiho.scaffold.logging.entity.convert.LogVOConvert;
import com.jcweiho.scaffold.logging.entity.criteria.LogQueryCriteria;
import com.jcweiho.scaffold.logging.enums.BusinessStatusEnum;
import com.jcweiho.scaffold.logging.enums.LogTypeEnum;
import com.jcweiho.scaffold.logging.mapper.LogMapper;
import com.jcweiho.scaffold.logging.service.LogService;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-06
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl extends CommonServiceImpl<LogMapper, Log> implements LogService {
    private final LogErrorVOConvert logErrorVOConvert;
    private final LogUserVOConvert logUserVOConvert;
    private final LogErrorUserVOConvert logErrorUserVOConvert;
    private final LogVOConvert logVOConvert;

    @Override
    public List<Log> findAll(LogQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Log.class, criteria)));
    }

    @Override
    public Map<String, Object> findAllByPage(LogQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
        PageInfo<Log> pageInfo = new PageInfo<>(findAll(criteria));
        Map<String, Object> map = null;
        if (!StringUtils.isBlank(criteria.getLogType())) {
            if (criteria.getLogType().equals(LogTypeEnum.INFO.getMsg())) {
                map = PageUtils.toPageContainer(logVOConvert.toPojo(pageInfo.getList()), pageInfo.getTotal());
            } else {
                map = PageUtils.toPageContainer(logErrorVOConvert.toPojo(pageInfo.getList()), pageInfo.getTotal());
            }
        }
        return map;
    }

    @Override
    @Async
    public void saveLogInfo(final JoinPoint joinPoint, HttpServletRequest request, Logging logging,
                            Log logInfo, final Exception e, Object jsonResult) {
        log.info("Log -> 开始收集操作日志信息");
        //设置操作状态
        if (e != null) {
            logInfo.setStatus(BusinessStatusEnum.FAIL);
            logInfo.setExceptionDetail(StringUtils.substring(ThrowableUtils.getStackTrace(e), 0, 6000));
        } else {
            logInfo.setStatus(BusinessStatusEnum.SUCCESS);
        }
        //设置请求方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        logInfo.setMethod(className + "." + methodName + "()");
        //处理注解上的信息
        getControllerMethodDescription(joinPoint, logging, request, logInfo, jsonResult);
        //存入数据库
        this.save(logInfo);
        log.info("Log -> 操作日志已保存");
    }

    @Override
    public void download(List<Log> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (Log log : logs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("操作用户", log.getUsername());
            map.put("操作描述", log.getTitle());
            map.put("业务类型", log.getBusinessType());
            map.put("请求方法名称", log.getMethod());
            map.put("请求方式", log.getRequestMethod());
            map.put("请求URL", log.getRequestUrl());
            map.put("请求IP", log.getRequestIp());
            map.put("请求的浏览器", log.getBrowser());
            map.put("IP所在地", log.getAddress());
            map.put("请求参数", log.getRequestParams());
            map.put("响应结果", log.getResponseResult());
            map.put("日志级别", log.getLogType());
            map.put("操作状态", log.getStatus());
            map.put("错误信息", StringUtils.isBlank(log.getExceptionDetail()) ? "" : log.getExceptionDetail());
            map.put("消耗时间", log.getTime());
            map.put("创建时间", log.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByInfo() {
        this.lambdaUpdate().set(Log::getIsDel, 1).eq(Log::getLogType, LogTypeEnum.INFO.getMsg()).eq(Log::getIsDel, 0).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByError() {
        this.lambdaUpdate().set(Log::getIsDel, 1).eq(Log::getLogType, LogTypeEnum.ERROR.getMsg()).eq(Log::getIsDel, 0).update();
    }

    @Override
    public Object findByErrorDetail(Long id) {
        Log log = this.getById(id);
        IdSecureUtils.verifyIdNotNull(log.getId());
        byte[] details = log.getExceptionDetail().getBytes();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }

    @Override
    public Map<String, Object> findByUsername(LogTypeEnum logType, Pageable pageable) {
        startPage(pageable);
        PageInfo<Log> pageInfo = new PageInfo<>(this.selectByUsername(SecurityUtils.getUsername(), logType.getMsg()));
        Map<String, Object> map;
        if (logType.equals(LogTypeEnum.INFO)) {
            map = PageUtils.toPageContainer(logUserVOConvert.toPojo(pageInfo.getList()), pageInfo.getTotal());
        } else {
            map = PageUtils.toPageContainer(logErrorUserVOConvert.toPojo(pageInfo.getList()), pageInfo.getTotal());
        }
        return map;
    }

    @Override
    public List<Log> selectByUsername(String username, String logType) {
        return this.lambdaQuery().eq(Log::getUsername, username).eq(Log::getLogType, logType).list();
    }

    /**
     * 获取注解基础信息，放入Log对象
     *
     * @param joinPoint  切点
     * @param logging    注解对象
     * @param logInfo    Log对象
     * @param jsonResult 响应结果
     */
    private void getControllerMethodDescription(JoinPoint joinPoint, Logging logging, HttpServletRequest request,
                                                Log logInfo, Object jsonResult) {
        //模块名称
        logInfo.setTitle(StringUtils.isBlank(logging.title()) ? logging.value() : logging.title());
        //设置业务类型
        logInfo.setBusinessType(logging.businessType());
        //是否需要保存请求参数信息
        if (logging.saveRequestData()) {
            //获取参数信息，放入对象中
            setRequestValue(joinPoint, request, logInfo);
        }
        //是否需要保存响应结果
        if (logging.saveResponseData() && jsonResult != null) {
            logInfo.setResponseResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 6000));
        }
    }

    /**
     * 获取请求的参数,放入Log对象中
     *
     * @param joinPoint 切入点
     * @param logInfo   Log对象
     */
    private void setRequestValue(JoinPoint joinPoint, HttpServletRequest request, Log logInfo) {
        /*
          判断是不是PUT或者POST请求
          如果请求类型是 PUT 或者 POST，就意味着请求参数是在请求体中，请求参数有可能是二进制数据（例如上传的文件），二进制数据就不好保存了，
          所以对于 POST 和 PUT 还是从接口参数中提取，然后过滤掉二进制数据即可
         */
        if (HttpMethod.PUT.name().equals(logInfo.getRequestMethod())
                || HttpMethod.POST.name().equals(logInfo.getRequestMethod())) {
            String params = argsArrayToString(joinPoint.getArgs());
            logInfo.setRequestParams(StringUtils.substring(params, 0, 6000));
        } else {
            /*
              如果请求类型是 GET 或者 DELETE，则请求参数就直接从请求对象提取了
             */
            Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (paramsMap == null) {
                logInfo.setRequestParams(null);
            } else {
                logInfo.setRequestParams(StringUtils.substring(paramsMap.toString(), 0, 6000));
            }
        }
    }

    /**
     * 参数的拼接
     *
     * @param paramsArray 传入的参数数组
     * @return 参数的字符串拼接
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = StringUtils.builder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (o != null && !isFilterObject(o)) {
                    Object jsonObj = JSON.toJSON(o);
                    params.append(jsonObj.toString()).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤参数
     *
     * @param obj 对象信息
     * @return 如果是需要过滤的对象，则返回true；否则返回false
     * 例如 HttpServletRequest、HttpServletResponse 或者文件上传
     * 对象 MultipartFile 等，这些类型的内容是不需要记录到日志中的
     */
    @SuppressWarnings("rawtypes")
    private boolean isFilterObject(final Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) obj;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) obj;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return obj instanceof MultipartFile
                || obj instanceof HttpServletRequest
                || obj instanceof HttpServletResponse
                || obj instanceof BindingResult;
    }
}
