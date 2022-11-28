package com.weiho.scaffold.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.weiho.scaffold.common.exception.BadRequestException;
import com.weiho.scaffold.common.util.FileUtils;
import com.weiho.scaffold.common.util.ListUtils;
import com.weiho.scaffold.common.util.PageUtils;
import com.weiho.scaffold.common.util.SecurityUtils;
import com.weiho.scaffold.common.util.result.VueSelectVO;
import com.weiho.scaffold.common.util.secure.IdSecureUtils;
import com.weiho.scaffold.i18n.I18nMessagesUtils;
import com.weiho.scaffold.mp.core.QueryHelper;
import com.weiho.scaffold.mp.enums.SortTypeEnum;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;
import com.weiho.scaffold.system.entity.Notice;
import com.weiho.scaffold.system.entity.User;
import com.weiho.scaffold.system.entity.convert.NoticeVOConvert;
import com.weiho.scaffold.system.entity.criteria.NoticeQueryCriteria;
import com.weiho.scaffold.system.entity.vo.NoticeVO;
import com.weiho.scaffold.system.mapper.NoticeMapper;
import com.weiho.scaffold.system.service.NoticeService;
import com.weiho.scaffold.system.service.UserService;
import lombok.RequiredArgsConstructor;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 通知信息表 服务实现类
 * </p>
 *
 * @author Weiho
 * @since 2022-11-02
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class NoticeServiceImpl extends CommonServiceImpl<NoticeMapper, Notice> implements NoticeService {
    private final NoticeVOConvert noticeVOConvert;
    private final UserService userService;

    @Override
    public List<NoticeVO> convertToVO(List<Notice> notices) {
        List<NoticeVO> noticeVOList = noticeVOConvert.toPojo(notices);
        for (NoticeVO noticeVO : noticeVOList) {
            User user = userService.getById(noticeVO.getUserId());
            noticeVO.setUsername(user.getUsername());
        }
        return noticeVOList;
    }

    @Override
    public List<Notice> findAll(NoticeQueryCriteria criteria) {
        return this.getBaseMapper().selectList(CastUtils.cast(QueryHelper.getQueryWrapper(Notice.class, criteria)));
    }


    @Override
    public Map<String, Object> getNoticeList(NoticeQueryCriteria criteria, Pageable pageable) {
        startPage(pageable, "create_time", SortTypeEnum.ASC);
        PageInfo<Notice> pageInfo = new PageInfo<>(this.findAll(criteria));
        return PageUtils.toPageContainer(this.convertToVO(pageInfo.getList()), pageInfo.getTotal());
    }

    @Override
    public void download(HttpServletResponse response, List<NoticeVO> all) throws IOException {
        List<Map<String, Object>> list = ListUtils.list(false);
        for (NoticeVO noticeVO : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(I18nMessagesUtils.get("download.notice.scope"), noticeVO.getType().getDisplay());
            map.put(I18nMessagesUtils.get("download.notice.title"), noticeVO.getTitle());
            map.put(I18nMessagesUtils.get("download.notice.content"), noticeVO.getContent());
            map.put(I18nMessagesUtils.get("download.notice.username"), noticeVO.getUsername());
            map.put(I18nMessagesUtils.get("download.createTime"), noticeVO.getCreateTime());
            list.add(map);
        }
        FileUtils.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateNotice(NoticeVO resources) {
        IdSecureUtils.verifyIdNotNull(resources.getId());
        Notice notice = this.getById(resources.getId());
        // 通知人不能修改
        if (!userService.getById(notice.getUserId()).getUsername().equals(resources.getUsername())) {
            throw new BadRequestException(I18nMessagesUtils.get("notice.tip1"));
        }
        notice.setType(resources.getType());
        notice.setTitle(resources.getTitle());
        notice.setContent(resources.getContent());
        notice.setIsOverdue(resources.getIsOverdue());
        notice.setUpdateUsername(SecurityUtils.getUsername());
        return this.saveOrUpdate(notice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addNotice(NoticeVO resources) {
        IdSecureUtils.verifyIdNull(resources.getId());
        // 构造实体
        Notice notice = new Notice();

        notice.setType(resources.getType());
        notice.setTitle(resources.getTitle());
        notice.setContent(resources.getContent());
        notice.setUserId(userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, SecurityUtils.getUsername())).getId());
        notice.setIsOverdue(resources.getIsOverdue());

        return this.save(notice);
    }

    @Override
    public List<VueSelectVO> getDistinctUserSelect() {
        List<VueSelectVO> list = ListUtils.list(false);
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct user_id");
        List<Long> userIds = this.getBaseMapper().selectList(queryWrapper).stream().map(Notice::getUserId).collect(Collectors.toList());
        for (Long userId : userIds) {
            list.add(new VueSelectVO(userId, userService.getById(userId).getUsername()));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotice(Set<Long> ids) {
        return this.removeByIds(ids);
    }
}
