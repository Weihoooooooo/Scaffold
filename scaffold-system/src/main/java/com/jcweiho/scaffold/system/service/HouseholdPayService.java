package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.HouseholdPay;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdPayQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdPayVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业主缴费信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2023-01-07
 */
public interface HouseholdPayService extends CommonService<HouseholdPay> {
    /**
     * 将查询结果转为特殊VO
     *
     * @param householdPays 业主缴费结果
     * @return 转换结果
     */
    List<HouseholdPayVO> convertToVO(List<HouseholdPay> householdPays);

    /**
     * 查询所有符合条件的业主缴费
     *
     * @param criteria 条件
     * @return 查询结果
     */
    List<HouseholdPay> findAll(HouseholdPayQueryCriteria criteria);

    /**
     * 查询所有符合条件的业主缴费(分页)
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return 分页结果
     */
    Map<String, Object> findAll(HouseholdPayQueryCriteria criteria, Pageable pageable);

    /**
     * 下载业主缴费
     *
     * @param response 响应参数
     * @param all      结果集
     */
    void download(List<HouseholdPayVO> all, HttpServletResponse response) throws IOException;

    /**
     * 添加业主缴费
     *
     * @param resources 业主缴费
     * @return 是否成功
     */
    boolean addHouseholdPay(HouseholdPay resources);

    /**
     * 删除业主缴费
     *
     * @param ids 通知ID
     * @return 是否成功
     */
    boolean deleteHouseholdPay(Set<Long> ids);
}
