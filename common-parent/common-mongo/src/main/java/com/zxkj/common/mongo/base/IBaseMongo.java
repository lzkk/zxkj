package com.zxkj.common.mongo.base;

import com.zxkj.common.mongo.page.MPage;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.List;

public interface IBaseMongo<T> {

    void save(T entity);

    void updateById(T entity);

    void updateByCondition(String[] propName, Object[] values, T entity);

    void updateByCondition(Criteria criteria, T entity);

    void updateByCondition(Query query, T entity);

    void deleteById(Serializable... ids);

    void deleteByCondition(String[] propName, Object[] values);

    void deleteByCondition(Criteria criteria);

    void deleteByCondition(Query query);

    List<T> find();

    List<T> findByCondition(String order);

    List<T> findByCondition(String propName, Object value);

    List<T> findByCondition(String propName, Object value, String order);

    List<T> findByCondition(String[] propName, Object[] values);

    List<T> findByCondition(String[] propName, Object[] values, String order);

    List<T> findByCondition(Criteria criteria);

    List<T> findByCondition(Criteria criteria, String order);

    List<T> findByCondition(Query query);

    MPage<T> findByPage(Criteria criteria, int pageNo, int pageSize);

    MPage<T> findByPage(Criteria criteria, String order, int pageNo, int pageSize);

    MPage<T> findByPage(Query query, int pageNo, int pageSize);

    T uniqueById(Serializable id);

    T uniqueByCondition(String propName, Object value);

    T uniqueByCondition(String[] propName, Object[] values);

    T uniqueByCondition(Criteria criteria);

    T uniqueByCondition(Query query);

    long countByCondition(String[] params, Object[] values);

    long countByCondition(Criteria criteria);

    long countByCondition(Query query);

}
