package com.zxkj.common.mongo.page;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 分页工具类
 * @date 2020/7/24 16:09
 */
public class MPage<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private Integer pageNo = 1;
    /**
     * 当前页条数
     */
    private Integer pageSize = 10;
    /**
     * 总共的条数
     */
    private Long total;
    /**
     * 总共的页数
     */
    private Integer pages;
    /**
     * 实体类集合
     */
    private List<T> list;

    public MPage() {

    }

    public MPage(Integer pageNo, Integer pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public MPage(Integer pageNo, Integer pageSize, Long total) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = getPages(total, pageSize);
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private int getPages(long total, int pageSize) {
        long pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        return (int) pages;
    }
}