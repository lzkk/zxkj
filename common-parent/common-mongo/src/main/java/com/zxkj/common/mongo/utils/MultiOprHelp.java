package com.zxkj.common.mongo.utils;

import com.zxkj.common.mongo.enums.MultiTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Mongo 多条件操作辅助类
 *
 * @author ：yuhui
 * @date ：Created in 2020/10/12 17:45
 */
public class MultiOprHelp {

    private MultiTypeEnum multiTypeEnum;

    private List<SingleOprHelp> list = new ArrayList<SingleOprHelp>();

    public MultiOprHelp(MultiTypeEnum multiTypeEnum, List<SingleOprHelp> list) {
        this.multiTypeEnum = multiTypeEnum;
        this.list = list;
    }

    public MultiOprHelp() {
    }

    public MultiOprHelp(List<SingleOprHelp> list) {
        this.list = list;
    }

    public MultiTypeEnum getMultiTypeEnum() {
        return multiTypeEnum;
    }

    public void setMultiTypeEnum(MultiTypeEnum multiTypeEnum) {
        this.multiTypeEnum = multiTypeEnum;
    }

    public List<SingleOprHelp> getList() {
        return list;
    }

    public void setList(List<SingleOprHelp> list) {
        this.list = list;
    }
}
