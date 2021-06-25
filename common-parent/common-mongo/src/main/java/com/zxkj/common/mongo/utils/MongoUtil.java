package com.zxkj.common.mongo.utils;

import com.zxkj.common.mongo.enums.MultiTypeEnum;
import com.zxkj.common.mongo.enums.SingleTypeEnum;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Mongo的工具类
 *
 * @author ：yuhui
 * @date ：Created in 2020/9/29 16:31
 */
public class MongoUtil {


    /**
     * 根据操作类型获得对应条件
     *
     * @param multiOprHelp
     * @return
     */
    public static Criteria[] getCriteriaArray(MultiOprHelp multiOprHelp) {
        if (multiOprHelp == null) {
            return null;
        }
        List<SingleOprHelp> list = multiOprHelp.getList();
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Criteria> criteriaList = new ArrayList<>();
        for (SingleOprHelp tmp : list) {
            Criteria tmpCriteria = getCriteria(tmp.getKey(), tmp.getSingleTypeEnum(), tmp.getValue());
            criteriaList.add(tmpCriteria);
        }
        Criteria[] tmpCri = new Criteria[criteriaList.size()];
        criteriaList.toArray(tmpCri);
        return tmpCri;
    }

    /**
     * 根据条件生成查询条件
     *
     * @param list
     * @return
     */
    public static Criteria getCriteria(List<MultiOprHelp> list) {
        List<Criteria> andCriteriaList = new ArrayList<>();
        List<Criteria> orCriteriaList = new ArrayList<>();
        for (MultiOprHelp multiOprHelp : list) {
            Criteria criArray = getCriteria(multiOprHelp);
            MultiTypeEnum multiTypeEnum = multiOprHelp.getMultiTypeEnum();
            if (MultiTypeEnum.OPERATE_AND == multiTypeEnum) {
                andCriteriaList.add(criArray);
            } else if (MultiTypeEnum.OPERATE_OR == multiTypeEnum) {
                orCriteriaList.add(criArray);
            }
        }
        Criteria finalCri = null;

        Criteria andCriteria = null;
        if (andCriteriaList != null && andCriteriaList.size() > 0) {
            Criteria[] tmpAndCri = new Criteria[andCriteriaList.size()];
            andCriteriaList.toArray(tmpAndCri);
            andCriteria = new Criteria().andOperator(tmpAndCri);
        }

        if (orCriteriaList == null || orCriteriaList.size() == 0) {
            finalCri = andCriteria;
        } else {
            if (andCriteria != null) {
                orCriteriaList.add(andCriteria);
            }
            Criteria[] tmpOrCri = new Criteria[orCriteriaList.size()];
            orCriteriaList.toArray(tmpOrCri);
            finalCri = new Criteria().orOperator(tmpOrCri);
        }
        return finalCri;
    }

    /**
     * 根据操作类型获得对应条件
     *
     * @param multiOprHelp
     * @return
     */
    public static Criteria getCriteria(MultiOprHelp multiOprHelp) {
        if (multiOprHelp == null) {
            return null;
        }
        List<SingleOprHelp> list = multiOprHelp.getList();
        if (list == null || list.size() == 0) {
            return null;
        }
        List<Criteria> criteriaList = new ArrayList<>();
        for (SingleOprHelp tmp : list) {
            Criteria tmpCriteria = getCriteria(tmp.getKey(), tmp.getSingleTypeEnum(), tmp.getValue());
            criteriaList.add(tmpCriteria);
        }
        Criteria[] tmpCri = new Criteria[criteriaList.size()];
        criteriaList.toArray(tmpCri);
        MultiTypeEnum multiTypeEnum = multiOprHelp.getMultiTypeEnum();
        if (MultiTypeEnum.OPERATE_AND == multiTypeEnum) {
            return new Criteria().andOperator(tmpCri);
        } else if (MultiTypeEnum.OPERATE_OR == multiTypeEnum) {
            return new Criteria().orOperator(tmpCri);
        } else {
            throw new RuntimeException("无效的多操作，" + multiTypeEnum);
        }
    }

    /**
     * 根据操作类型获得对应条件
     *
     * @param key
     * @param singleTypeEnum
     * @param value
     * @return
     */
    public static Criteria getCriteria(String key, SingleTypeEnum singleTypeEnum, Object value) {
        Criteria criteria = Criteria.where(key);
        if (SingleTypeEnum.OPERATE_IS == singleTypeEnum) { // 等于
            criteria = criteria.is(value);
        } else if (SingleTypeEnum.OPERATE_IS_NOT == singleTypeEnum) { // 不等于
            criteria = criteria.ne(value);
        } else if (SingleTypeEnum.OPERATE_LIKE == singleTypeEnum) { // 模糊查询
            Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
            criteria = criteria.regex(String.valueOf(pattern));
        } else if (SingleTypeEnum.OPERATE_IN == singleTypeEnum) { // in
            criteria = criteria.in(getInTypeObjectArray(value));
        } else if (SingleTypeEnum.OPERATE_NOT_IN == singleTypeEnum) { // not in
            criteria = criteria.nin(getInTypeObjectArray(value));
        } else if (SingleTypeEnum.OPERATE_GT == singleTypeEnum) { // 大于
            criteria = criteria.gt(value);
        } else if (SingleTypeEnum.OPERATE_GTE == singleTypeEnum) { // 大于等于
            criteria = criteria.gte(value);
        } else if (SingleTypeEnum.OPERATE_LT == singleTypeEnum) { // 小于
            criteria = criteria.lt(value);
        } else if (SingleTypeEnum.OPERATE_LTE == singleTypeEnum) { // 小于等于
            criteria = criteria.lte(value);
        } else {
            throw new RuntimeException("不支持的操作类型！" + singleTypeEnum);
        }
        return criteria;
    }

    /**
     * 返回处理in操作的对象数组
     *
     * @param value
     * @return
     */
    private static Object[] getInTypeObjectArray(Object value) {
        Object[] tmp = null;
        if (value instanceof Object[]) {
            tmp = (Object[]) value;
        } else if (value instanceof List) {
            List tmpList = (List) value;
            tmp = new Object[tmpList.size()];
            tmpList.toArray(tmp);
        } else {
            // 其他数据类型，不识别，不处理
            return new Object[0];
        }
        return tmp;
    }

    /**
     * like模糊匹配
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getLikeCriteria(String key, Object value) {
        Pattern pattern = Pattern.compile("^.*" + value + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where(key).regex(pattern);
        return criteria;
    }

    /**
     * in查询
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getInCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).in(value);
        return criteria;
    }

    /**
     * not in查询
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getNotInCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).nin(value);
        return criteria;
    }

    /**
     * 等值查询is
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getIsCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).is(value);
        return criteria;
    }


    /**
     * 不等值查询is not
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getIsNotCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).ne(value);
        return criteria;
    }

    /**
     * 大于等于gte
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getGteCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).gte(value);
        return criteria;
    }

    /**
     * 大于gt
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getGtCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).gt(value);
        return criteria;
    }


    /**
     * 小于等于lte
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getLteCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).lte(value);
        return criteria;
    }

    /**
     * 小于lt
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria getLtCriteria(String key, Object value) {
        Criteria criteria = Criteria.where(key).lt(value);
        return criteria;
    }

    /**
     * 生成and操作集合
     *
     * @param list
     * @return
     */
    public static MultiOprHelp getAnd(List<SingleOprHelp> list) {
        return new MultiOprHelp(MultiTypeEnum.OPERATE_AND, list);
    }

    /**
     * 生成or操作集合
     *
     * @param list
     * @return
     */
    public static MultiOprHelp getOr(List<SingleOprHelp> list) {
        return new MultiOprHelp(MultiTypeEnum.OPERATE_OR, list);
    }

    /**
     * 返回and操作集合列表
     *
     * @param andList
     * @return
     */
    public static List<MultiOprHelp> queryMultiList(List<SingleOprHelp> andList) {
        return queryMultiList(andList, null);
    }

    /**
     * 返回操作集合列表
     *
     * @param andList
     * @param orList
     * @return
     */
    public static List<MultiOprHelp> queryMultiList(List<SingleOprHelp> andList, List<SingleOprHelp> orList) {
        List<MultiOprHelp> multiList = new ArrayList<MultiOprHelp>();
        if (andList != null && andList.size() > 0) {
            multiList.add(MongoUtil.getAnd(andList));
        }
        if (orList != null && orList.size() > 0) {
            multiList.add(MongoUtil.getOr(orList));
        }
        return multiList;
    }
}
