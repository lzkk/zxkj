package com.zxkj.common.page;


import java.util.List;

/**
 * @Description 通用分页组件类
 */
public class PagedList<T> {

    private long pageNo = 1;

    private long pageSize = 20;

    private long totalRows = 0;

    private List<T> data;

    public PagedList() {
        super();
    }

    public PagedList(long totalRows, List<T> data) {
        super();
        this.totalRows = totalRows;
        this.data = data;
    }

    public PagedList(long pageNo, long totalRows, List<T> data) {
        this(totalRows, data);
        this.data = data;
    }

    public PagedList(long pageNo, long pageSize, long totalRows, List<T> data) {
        this(pageNo, totalRows, data);
        this.pageSize = pageSize;
    }

    public long getTotalPages() {
        if (pageSize == 0) {
            return 0;
        }
        return (long) Math.ceil(totalRows * 1.0D / pageSize);
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PagedList [pageNo=" + pageNo + ", pageSize=" + pageSize + ", totalRows=" + totalRows + ", data=" + data
                + "]";
    }

}