package com.jcweiho.scaffold.logging.service;

import com.jcweiho.scaffold.logging.entity.Log;
import com.jcweiho.scaffold.logging.entity.criteria.LogQueryCriteria;
import com.jcweiho.scaffold.logging.enums.LogTypeEnum;
import com.jcweiho.scaffold.mp.service.CommonService;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-08-06
 */
public interface LogService extends CommonService<Log> {

    /**
     * 根据条件查询所有数据
     *
     * @param criteria 条件实体
     * @return List<Log>
     */
    List<Log> findAll(LogQueryCriteria criteria);

    /**
     * 根据条件查询所有数据
     *
     * @param criteria 条件实体
     * @param pageable 分页实体
     * @return 带有结果列表和总条数的Map
     */
    Map<String, Object> findAllByPage(LogQueryCriteria criteria, Pageable pageable);

    /**
     * 存储日志信息
     *
     * @param log Log对象
     */
    @Async
    void saveLogInfo(Log log);

    /**
     * 导出日志
     *
     * @param logs     待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<Log> logs, HttpServletResponse response) throws IOException;

    /**
     * 删除所有的INFO日志
     */
    void deleteAllByInfo();

    /**
     * 删除所有的ERROR日志
     */
    void deleteAllByError();

    /**
     * 根据ID获取ERROR的错误信息
     *
     * @param id ID
     * @return /
     */
    Object findByErrorDetail(Long id);

    /**
     * 根据当前登录的用户名查找操作记录
     *
     * @param logType  日志类型
     * @param pageable 分页信息
     * @return /
     */
    Map<String, Object> findByUsername(LogTypeEnum logType, Pageable pageable);

    /**
     * 查找对应用户名的对应类别的日志
     *
     * @param username 用户名
     * @param logType  日志的级别
     * @return /
     */
    List<Log> selectByUsername(String username, String logType);
}
