package com.zxkj.common.condition;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页条件
 */
@Data
public class PageCondition extends MobileCondition implements Serializable {

    /**
     * 默认起始页
     */
    public static final Integer defaultStartPage = 1;

    /**
     * 默认每页记录数
     */
    public static final Integer defaultPageSize = 10;

    protected Integer pageNum = 1;

    protected Integer pageSize = 10;

}
