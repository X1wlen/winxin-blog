package com.winxin.blog.common.result;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Getter;
import lombok.Setter;

/**
 * 统一分页响应结构：分页列表接口的 data 一律使用该结构，
 * 前端按 list/total/page/pageSize 解析（与具体 ORM 分页对象解耦）。
 *
 * @param <T> 行记录类型
 */
@Getter
@Setter
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前页数据 */
    private List<T> list;

    /** 总记录数 */
    private long total;

    /** 当前页码（从 1 开始） */
    private long page;

    /** 每页条数 */
    private long pageSize;

    public static <T> PageResult<T> of(List<T> list, long total, long page, long pageSize) {
        PageResult<T> result = new PageResult<>();
        result.list = list;
        result.total = total;
        result.page = page;
        result.pageSize = pageSize;
        return result;
    }

    /** 由 MyBatis-Plus 分页查询结果直接转换（业务实现时配合 Page<T> 使用） */
    public static <T> PageResult<T> from(IPage<T> page) {
        return of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }
}
