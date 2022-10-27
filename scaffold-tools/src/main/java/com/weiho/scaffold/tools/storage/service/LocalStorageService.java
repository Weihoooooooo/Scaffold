package com.weiho.scaffold.tools.storage.service;

import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.tools.storage.entity.LocalStorage;
import com.weiho.scaffold.tools.storage.entity.criteria.LocalStorageQueryCriteria;
import com.weiho.scaffold.tools.storage.entity.vo.LocalStorageVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

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
 * @since 2022-10-27
 */
public interface LocalStorageService extends CommonService<LocalStorage> {
    /**
     * 文件上传
     *
     * @param multipartFile 文件
     * @param filename      上传的文件名
     * @return picture
     */
    LocalStorage upload(MultipartFile multipartFile, String filename);

    /**
     * 查询文件列表,不分页
     *
     * @param criteria 查询条件
     * @return /
     */
    List<LocalStorage> findAll(LocalStorageQueryCriteria criteria);

    /**
     * 查询文件列表,分页
     *
     * @param criteria 查询条件
     * @param pageable 分页
     * @return /
     */
    Map<String, Object> findAll(LocalStorageQueryCriteria criteria, Pageable pageable);

    /**
     * 导出数据
     *
     * @param all      导出的数据
     * @param response 响应
     */
    void download(List<LocalStorage> all, HttpServletResponse response) throws IOException;

    /**
     * 删除图片
     *
     * @param ids 图片的主键
     * @return boolean
     */
    boolean deleteByIds(Long[] ids);

    /**
     * 修改文件名字
     *
     * @param localStorageVO 前端VO
     * @return /
     */
    boolean updateFileName(LocalStorageVO localStorageVO);
}
