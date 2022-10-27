package com.weiho.scaffold.tools.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.date.DateUtils;
import com.weiho.scaffold.common.util.date.FormatEnum;
import com.weiho.scaffold.common.util.file.FileUtils;
import com.weiho.scaffold.common.util.message.I18nMessagesUtils;
import com.weiho.scaffold.common.util.security.SecurityUtils;
import com.weiho.scaffold.common.util.string.StringUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.tools.storage.entity.LocalStorage;
import com.weiho.scaffold.tools.storage.entity.criteria.LocalStorageQueryCriteria;
import com.weiho.scaffold.tools.storage.entity.vo.LocalStorageVO;
import com.weiho.scaffold.tools.storage.mapper.LocalStorageMapper;
import com.weiho.scaffold.tools.storage.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.CastUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-10-27
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LocalStorageServiceImpl extends CommonServiceImpl<LocalStorageMapper, LocalStorage> implements LocalStorageService {
    private final ScaffoldSystemProperties properties;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public LocalStorage upload(MultipartFile multipartFile, String filename) {
        if (multipartFile.isEmpty()) {
            throw new BadRequestException(I18nMessagesUtils.get("file.null.tip"));
        }

        // 检查文件的大小(最大50M)
        FileUtils.checkSize(properties.getResourcesProperties().getStorageUploadMaxSize(), multipartFile.getSize());
        // 获取文件后缀
        String suffix = FileUtils.getExtensionName(multipartFile.getOriginalFilename());
        // 处理上传的文件,保存文件
        File file = FileUtils.upload(multipartFile, properties.getResourcesProperties().getStorageLocalAddressPrefix());
        // 构建文件体
        LocalStorage localStorage = new LocalStorage();
        localStorage.setUsername(SecurityUtils.getUsername());
        localStorage.setFileName(StringUtils.isBlank(filename) ? FileUtils.getFileNameNoEx(file.getName()) : filename);
        localStorage.setRealName(file.getName());
        localStorage.setType(FileUtils.getFileType(suffix));
        localStorage.setSize(FileUtils.getSize(multipartFile.getSize()));
        localStorage.setSuffix(suffix);
        localStorage.setServerUrl(properties.getResourcesProperties().getStorageServerAddressPrefix() + file.getName());
        localStorage.setLocalUrl(properties.getResourcesProperties().getStorageLocalAddressPrefix() + file.getName());
        localStorage.setMd5Code(FileUtils.getMd5(file));
        localStorage.setCreateTime(DateUtils.getNowDate());

        if (this.save(localStorage)) {
            return localStorage;
        } else {
            return new LocalStorage();
        }
    }

    @Override
    public List<LocalStorage> findAll(LocalStorageQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(criteria, LocalStorage.class)));
    }

    @Override
    public Map<String, Object> findAll(LocalStorageQueryCriteria criteria, Pageable pageable) {
        startPage(pageable);
//        return toPageContainer(pageable, this.findAll(criteria));
        return toPageContainer(this.findAll(criteria));
    }

    @Override
    public void download(List<LocalStorage> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorage localStorage : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("操作人", localStorage.getUsername());
            map.put("文件名", localStorage.getFileName());
            map.put("文件真实名", localStorage.getRealName());
            map.put("文件类型", localStorage.getType());
            map.put("文件大小", localStorage.getSize());
            map.put("文件后缀", localStorage.getSuffix());
            map.put("文件服务器地址", localStorage.getServerUrl());
            map.put("文件本地地址", localStorage.getLocalUrl());
            map.put("图片的MD5值", localStorage.getMd5Code());
            map.put("上传时间", DateUtils.parseDateToStr(FormatEnum.YYYY_MM_DD_HH_MM_SS, localStorage.getCreateTime()));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(Long[] ids) {
        for (Long id : ids) {
            LocalStorage localStorage = this.getById(id);
            if (localStorage != null && localStorage.getLocalUrl() != null) {
                boolean flag = FileUtils.del(localStorage.getLocalUrl());
                if (flag) {
                    return this.removeById(id);
                }
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFileName(LocalStorageVO localStorageVO) {
        LocalStorage localStorage = this.getById(localStorageVO.getId());

        LocalStorage localStorageFileName = this.getOne(new LambdaQueryWrapper<LocalStorage>().eq(LocalStorage::getFileName, localStorageVO.getFileName()));

        if (localStorageFileName != null && !localStorage.getId().equals(localStorageFileName.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("file.exist.tip"));
        }

        if (!localStorage.getFileName().equals(localStorageVO.getFileName())) {
            localStorage.setFileName(localStorageVO.getFileName());
            return this.saveOrUpdate(localStorage);
        } else {
            return true;
        }
    }
}
