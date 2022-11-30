package com.jcweiho.scaffold.tools.storage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jcweiho.scaffold.common.config.system.ScaffoldSystemProperties;
import com.jcweiho.scaffold.common.exception.BadRequestException;
import com.jcweiho.scaffold.common.util.*;
import com.jcweiho.scaffold.i18n.I18nMessagesUtils;
import com.jcweiho.scaffold.mp.core.QueryHelper;
import com.jcweiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.jcweiho.scaffold.tools.storage.entity.LocalStorage;
import com.jcweiho.scaffold.tools.storage.entity.criteria.LocalStorageQueryCriteria;
import com.jcweiho.scaffold.tools.storage.entity.vo.LocalStorageVO;
import com.jcweiho.scaffold.tools.storage.mapper.LocalStorageMapper;
import com.jcweiho.scaffold.tools.storage.service.LocalStorageService;
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
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        if (StringUtils.isNotBlank(filename) && filename.equals("undefined")) {
            LocalStorage localStorageFileName = this.getOne(new LambdaQueryWrapper<LocalStorage>()
                    .eq(LocalStorage::getFileName, filename));
            if (localStorageFileName != null) {
                throw new BadRequestException(I18nMessagesUtils.get("file.exist.tip"));
            }
        }

        // 检查文件的大小(最大50M)
        FileUtils.checkSize(properties.getResourcesProperties().getStorageUploadMaxSize(), multipartFile.getSize());
        // 获取文件后缀
        String suffix = FileUtils.getExtensionName(multipartFile.getOriginalFilename());
        // 获取文件的分类
        String type = FileUtils.getFileType(suffix);
        // 处理上传的文件,保存文件
        File file = FileUtils.upload(multipartFile, properties.getResourcesProperties().getStorageLocalAddressPrefix() + type + "\\");
        if (ObjectUtil.isNull(file)) {
            throw new BadRequestException(I18nMessagesUtils.get("upload.error"));
        }

        // 构建文件体
        LocalStorage localStorage = new LocalStorage();
        localStorage.setUsername(SecurityUtils.getUsername());
        localStorage.setFileName(StringUtils.isBlank(filename) || filename.equals("undefined") ? FileUtils.getFileNameNoEx(file.getName()) : filename);
        localStorage.setRealName(file.getName());
        localStorage.setType(type);
        localStorage.setSize(FileUtils.getSize(multipartFile.getSize()));
        localStorage.setSuffix(suffix);
        localStorage.setServerUrl(properties.getResourcesProperties().getStorageServerAddressPrefix() + type + "/" + file.getName());
        localStorage.setLocalUrl(properties.getResourcesProperties().getStorageLocalAddressPrefix() + type + "\\" + file.getName());
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
        return toPageContainer(this.findAll(criteria));
    }

    @Override
    public void download(List<LocalStorage> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
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
            map.put("上传时间", DateUtils.parseDateToStr(DateUtils.FormatEnum.YYYY_MM_DD_HH_MM_SS, localStorage.getCreateTime()));
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(Set<Long> ids) {
        for (Long id : ids) {
            LocalStorage localStorage = this.getById(id);
            if (ObjectUtil.isNotNull(localStorage) && StringUtils.isNotBlank(localStorage.getLocalUrl())) {
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

        if (ObjectUtil.isNotNull(localStorageFileName) && !localStorage.getId().equals(localStorageFileName.getId())) {
            throw new BadRequestException(I18nMessagesUtils.get("file.exist.tip"));
        }

        if (!localStorage.getFileName().equals(localStorageVO.getFileName())) {
            localStorage.setFileName(localStorageVO.getFileName());
            return this.saveOrUpdate(localStorage);
        } else {
            return true;
        }
    }

    @Override
    public void download(Serializable localStorageId, HttpServletResponse response) throws IOException {
        // 根据主键查找数据库
        LocalStorage localStorage = this.getById(localStorageId);
        String fileLocalPath = localStorage.getLocalUrl();
        FileUtils.downloadFile(fileLocalPath, response);
    }
}
