package com.zxkj.common.mongo.utils;

import org.springframework.data.mongodb.core.query.Criteria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 工具类
 * @date 2020/7/24 16:09
 */
public class MongoOperateMerge implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Criteria> andCriteriaList = new ArrayList<>();
    private List<Criteria> orCriteriaList = new ArrayList<>();

    public static MongoOperateMerge empty() {
        return new MongoOperateMerge();
    }

    public Criteria collect() {
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

    public MongoOperateMerge or(MultiOprHelp multiOprHelp) {
        Criteria tmpCri = MongoUtil.getCriteria(multiOprHelp);
        if (tmpCri != null) {
            orCriteriaList.add(tmpCri);
        }
        return this;
    }

    public MongoOperateMerge and(MultiOprHelp multiOprHelp) {
        Criteria tmpCri = MongoUtil.getCriteria(multiOprHelp);
        if (tmpCri != null) {
            andCriteriaList.add(tmpCri);
        }
        return this;
    }
}