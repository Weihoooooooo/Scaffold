package com.jcweiho.scaffold.logging.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.common.util.secure.IdSecureUtils;
import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.logging.entity.convert.LogErrorUserVOConvert;
import com.jcweiho.scaffold.logging.entity.convert.LogErrorVOConvert;
import com.jcweiho.scaffold.logging.entity.convert.LogUserVOConvert;
import com.jcweiho.scaffold.logging.entity.convert.LogVOConvert;
import com.jcweiho.scaffold.logging.entity.criteria.LogQueryCriteria;
import com.jcweiho.scaffold.logging.enums.LogTypeEnum;
import com.jcweiho.scaffold.logging.mapper.LogMapper;
import com.jcweiho.scaffold.logging.service.LogService;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void saveLogInfo(Log logInfo) {
        log.info("Log -> 保存日志");
        this.save(logInfo);
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

}
