package com.zxkj.common.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * easyExcel导入规则校验辅助类
 *
 * @author ：yuhui
 * @date ：Created in 2020/12/3 10:05
 */
public class EasyExcelRuleCheckHelp {

    public EasyExcelRuleCheckHelp(Class clz) {
        this.fieldIndexMap = getExcelFieldIndexMap(clz);
        if (this.fieldIndexMap != null) {
            this.notNullKeyList = new ArrayList<>(this.fieldIndexMap.keySet());
        }
    }

    public EasyExcelRuleCheckHelp(Class clz, List<String> valueEqualKeyList) {
        this.valueEqualKeyList = valueEqualKeyList;
        this.fieldIndexMap = getExcelFieldIndexMap(clz);
        if (this.fieldIndexMap != null) {
            this.notNullKeyList = new ArrayList<>(this.fieldIndexMap.keySet());
        }
    }

    public EasyExcelRuleCheckHelp(Class clz, List<String> valueEqualKeyList, Integer cols) {
        this.fieldIndexMap = getExcelFieldIndexMap(clz);
        if (this.fieldIndexMap != null) {
            this.notNullKeyList = new ArrayList<>(this.fieldIndexMap.keySet());
        }
        this.valueEqualKeyList = valueEqualKeyList;
        this.cols = cols;
    }

    public EasyExcelRuleCheckHelp(Class clz, List<String> notNullKeyList, List<String> valueEqualKeyList) {
        this.notNullKeyList = notNullKeyList;
        this.valueEqualKeyList = valueEqualKeyList;
        this.fieldIndexMap = getExcelFieldIndexMap(clz);
    }

    public EasyExcelRuleCheckHelp(Class clz, List<String> notNullKeyList, List<String> valueEqualKeyList, Integer cols) {
        this.notNullKeyList = notNullKeyList;
        this.valueEqualKeyList = valueEqualKeyList;
        this.fieldIndexMap = getExcelFieldIndexMap(clz);
        this.cols = cols;
    }

    // 配置不能为空的列列表
    private List<String> notNullKeyList;
    // 配置必须等值的列列表
    private List<String> valueEqualKeyList;
    // 配置读取文件的强制要求列数
    private Integer cols;
    // 配置读取文件的字段与index对应关系
    private Map<String, Integer> fieldIndexMap;

    public List<String> getNotNullKeyList() {
        return notNullKeyList;
    }

    public void setNotNullKeyList(List<String> notNullKeyList) {
        this.notNullKeyList = notNullKeyList;
    }

    public List<String> getValueEqualKeyList() {
        return valueEqualKeyList;
    }

    public void setValueEqualKeyList(List<String> valueEqualKeyList) {
        this.valueEqualKeyList = valueEqualKeyList;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public Map<String, Integer> getFieldIndexMap() {
        return fieldIndexMap;
    }

    public void setFieldIndexMap(Map<String, Integer> fieldIndexMap) {
        this.fieldIndexMap = fieldIndexMap;
    }

    private Map<String, Integer> getExcelFieldIndexMap(Class clz) {
        Map<String, Integer> resultMap = new HashMap<>();
        try {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                Annotation annotation = field.getAnnotation(ExcelProperty.class);
                int index = ((ExcelProperty) annotation).index();
                resultMap.put(field.getName(), index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}
