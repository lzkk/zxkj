package com.zxkj.common.util.excel;

import java.util.List;

/**
 * easyExcel统计辅助类
 *
 * @author ：yuhui
 * @date ：Created in 2020/12/3 10:05
 */
public class EasyExcelStatHelp<T> {
    private Integer sucRecordCount;

    private Integer errRecordCount;

    private String errMsg;

    private List<T> data;

    public EasyExcelStatHelp() {
    }

    public EasyExcelStatHelp(Integer sucRecordCount, Integer errRecordCount, String errMsg) {
        this.sucRecordCount = sucRecordCount;
        this.errRecordCount = errRecordCount;
        this.errMsg = errMsg;
    }

    public EasyExcelStatHelp(Integer sucRecordCount, Integer errRecordCount, String errMsg, List<T> data) {
        this.sucRecordCount = sucRecordCount;
        this.errRecordCount = errRecordCount;
        this.errMsg = errMsg;
        this.data = data;
    }

    public Integer getSucRecordCount() {
        return sucRecordCount;
    }

    public void setSucRecordCount(Integer sucRecordCount) {
        this.sucRecordCount = sucRecordCount;
    }

    public Integer getErrRecordCount() {
        return errRecordCount;
    }

    public void setErrRecordCount(Integer errRecordCount) {
        this.errRecordCount = errRecordCount;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
