package com.jcweiho.scaffold.system.service;

import com.jcweiho.scaffold.common.util.result.VueSelectVO;
import com.jcweiho.scaffold.mp.service.CommonService;
import com.jcweiho.scaffold.system.entity.Household;
import com.jcweiho.scaffold.system.entity.criteria.HouseholdQueryCriteria;
import com.jcweiho.scaffold.system.entity.vo.HouseholdVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 梯户信息表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-12-13
 */
public interface HouseholdService extends CommonService<Household> {
    /**
     * 将查询结果转为特殊VO
     *
     * @param households 梯户信息表结果
     * @return 转换结果
     */
    List<HouseholdVO> convertToVO(List<Household> households);

    /**
     * 查询所有符合条件的梯户
     *
     * @param criteria 条件
     * @return 查询结果
     */
    List<Household> findAll(HouseholdQueryCriteria criteria);

    /**
     * 查询所有符合条件的梯户(分页)
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return 分页结果
     */
    Map<String, Object> findAll(HouseholdQueryCriteria criteria, Pageable pageable);

    /**
     * 下载梯户
     *
     * @param response 响应参数
     * @param all      结果集
     */
    void download(List<HouseholdVO> all, HttpServletResponse response) throws IOException;

    /**
     * 更新梯户
     *
     * @param resources 新梯户
     * @return 是否成功
     */
    boolean updateHousehold(HouseholdVO resources);

    /**
     * 添加梯户
     *
     * @param resources 梯户
     * @return 是否成功
     */
    boolean addHousehold(HouseholdVO resources);

    /**
     * 删除梯户
     *
     * @param ids 通知ID
     * @return 是否成功
     */
    boolean deleteHousehold(Set<Long> ids);

    /**
     * 根据楼宇ID查找梯户列表
     *
     * @param buildingId 楼宇ID
     * @return 梯户列表
     */
    List<VueSelectVO> getDistinctHouseholdSelect(Long buildingId);
}
