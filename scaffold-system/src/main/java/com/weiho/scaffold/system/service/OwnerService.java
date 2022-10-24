package com.weiho.scaffold.system.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.system.entity.Owner;
import com.weiho.scaffold.system.entity.criteria.OwnerQueryCriteria;
import com.weiho.scaffold.system.entity.vo.OwnerVO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业主表 服务类
 * </p>
 *
 * @author Weiho
 * @since 2022-10-21
 */
public interface OwnerService extends CommonService<Owner> {
    /**
     * 查询符合条件的业主列表
     *
     * @param criteria 查询条件
     * @return /
     */
    List<OwnerVO> findAll(OwnerQueryCriteria criteria);

    /**
     * 获取业主列表(分页)
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    Map<String, Object> getOwnerList(OwnerQueryCriteria criteria, Pageable pageable);

    /**
     * 导出业主列表信息
     *
     * @param all      表头
     * @param response 响应参数
     */
    void download(List<OwnerVO> all, HttpServletResponse response) throws IOException;

    /**
     * 新增业主
     *
     * @param ownerVO 业主信息
     * @return /
     */
    @SuppressWarnings("all")
    boolean createOwner(OwnerVO ownerVO);
}
