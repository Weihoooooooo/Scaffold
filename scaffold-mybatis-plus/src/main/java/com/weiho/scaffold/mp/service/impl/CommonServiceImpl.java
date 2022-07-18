package com.weiho.scaffold.mp.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.weiho.scaffold.mp.enums.SortTypeEnum;
import com.weiho.scaffold.mp.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

/**
 * 通用Service实现类
 *
 * @param <M> Mapper类
 * @param <E> 实体类
 */
@Slf4j
public abstract class CommonServiceImpl<M extends BaseMapper<E>, E> extends ServiceImpl<M, E> implements CommonService<E> {
    /**
     * 通用分页方法
     *
     * <p>
     * pageable.getPageNumber() 获取前端传入的当前页面(从0开始)
     * pageable.getPageSize()   获取前端传入的页面大小(即每页显示多少条信息)
     * </p>
     *
     * @param pageable 分页参数接收 (page,size,sort)
     */
    protected void getPage(Pageable pageable) {
        String order;
        order = pageable.getSort().toString();//获取排序信息
        order = order.replace(":", "");//拆分前端传入的字段 id:asc -> id asc
        if ("UNSORTED".equals(order)) {
            order = "id asc";//若没填写sort参数,则默认按照id升序
        }
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize(), order);
    }


    /**
     * 通用分页方法
     *
     * <p>
     * pageable.getPageNumber() 获取前端传入的当前页面(从0开始)
     * pageable.getPageSize()   获取前端传入的页面大小(即每页显示多少条信息)
     * </p>
     *
     * @param pageable              分页参数接收 (page,size,sort)
     * @param orderByDataBaseColumn 要按照数据库中的哪一个字段排序
     * @param sortBy                排序方式（asc,desc）
     */
    protected void getPage(Pageable pageable, String orderByDataBaseColumn, SortTypeEnum sortBy) {
        String order;
        order = pageable.getSort().toString();//获取排序信息
        order = order.replace(":", "");//拆分前端传入的字段 id:asc -> id asc
        if ("UNSORTED".equals(order)) {
            order = orderByDataBaseColumn + " " + sortBy.getSort();
        }
        PageHelper.startPage(pageable.getPageNumber() + 1, pageable.getPageSize(), order);
    }
}
