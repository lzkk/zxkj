package com.zxkj.common.mongo.base;

import com.zxkj.common.mongo.page.MPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Guo Mengya
 * @version 1.0.0
 * @desc mongo实现类
 * @date 2021-09-26 13:44:05
 */

public abstract class BaseMongoImpl<T> implements IBaseMongo<T> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    /**
     * 保存数据
     *
     * @param entity
     */
    @Override
    public void save(T entity) {
        mongoTemplate.save(entity);
    }


    /**
     * 根据主键ID修改数据
     *
     * @param entity
     */
    @Override
    public void updateById(T entity) {
        Update update = new Update();
        Query query = initialUpdateById(entity, update, false);
        if (query == null) {
            throw new RuntimeException("没有主键ID，无法修改");
        }
        mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    /**
     * 根据主键ID修改数据
     *
     * @param entity
     */
    @Override
    public void updateDynamicById(T entity) {
        Update update = new Update();
        Query query = initialUpdateById(entity, update, true);
        if (query == null) {
            throw new RuntimeException("没有主键ID，无法修改");
        }
        mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    /**
     * 根据条件修改数据
     *
     * @param propName
     * @param values
     * @param entity
     */
    @Override
    public void updateByCondition(String[] propName, Object[] values, T entity) {
        Query query = createQuery(propName, values, null);
        Update update = new Update();
        flushUpdate(entity, update, false);
        mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    /**
     * 根据查询修改数据
     *
     * @param criteria
     * @param entity
     */
    @Override
    public void updateByCondition(Criteria criteria, T entity) {
        Update update = new Update();
        flushUpdate(entity, update, false);
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    /**
     * 根据查询修改数据
     *
     * @param query
     * @param entity
     */
    @Override
    public void updateByCondition(Query query, T entity) {
        Update update = new Update();
        flushUpdate(entity, update, false);
        mongoTemplate.updateMulti(query, update, getEntityClass());
    }

    /**
     * 根据查询修改数据
     *
     * @param query
     * @param entity
     */
    @Override
    public void updateDynamicByCondition(Query query, T entity) {
        Update update = new Update();
        flushUpdate(entity, update, true);
        mongoTemplate.updateMulti(query, update, getEntityClass());
    }


    /**
     * 根据主键ID删除数据
     *
     * @param ids
     */
    @Override
    public void deleteById(Serializable... ids) {
        if (ids != null && ids.length > 0) {
            for (Serializable id : ids) {
                mongoTemplate.remove(mongoTemplate.findById(id, getEntityClass()));
            }
        }
    }

    /**
     * 根据指定的条件删除数据
     *
     * @param propName
     * @param values
     */
    @Override
    public void deleteByCondition(String[] propName, Object[] values) {
        Query query = createQuery(propName, values, null);
        mongoTemplate.remove(query, getEntityClass());
    }

    /**
     * 根据查询删除数据
     *
     * @param criteria
     */
    @Override
    public void deleteByCondition(Criteria criteria) {
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        mongoTemplate.remove(query, getEntityClass());
    }

    /**
     * 根据查询删除数据
     *
     * @param query
     */
    @Override
    public void deleteByCondition(Query query) {
        mongoTemplate.remove(query, getEntityClass());
    }


    /**
     * 查询所有数据
     *
     * @return
     */
    @Override
    public List<T> find() {
        return mongoTemplate.findAll(getEntityClass());
    }

    /**
     * 根据排序规则查询所有数据
     *
     * @param order
     * @return
     */
    @Override
    public List<T> findByCondition(String order) {
        List<Order> orderList = parseOrder(order);
        if (orderList == null || orderList.size() == 0) {
            return find();
        }
        return mongoTemplate.find(new Query().with(Sort.by(orderList)), getEntityClass());
    }

    /**
     * 根据指定条件查询数据
     *
     * @param propName
     * @param value
     * @return
     */
    @Override
    public List<T> findByCondition(String propName, Object value) {
        return findByCondition(propName, value, null);
    }

    /**
     * 根据条件查询数据
     *
     * @param propName
     * @param value
     * @param order
     * @return
     */
    @Override
    public List<T> findByCondition(String propName, Object value, String order) {
        Query query = new Query();
        query.addCriteria(Criteria.where(propName).is(value));
        List<Order> orderList = parseOrder(order);
        if (orderList != null && orderList.size() > 0) {
            query.with(Sort.by(orderList));
        }
        return mongoTemplate.find(query, getEntityClass());
    }

    /**
     * 根据条件查询数据
     *
     * @param propName
     * @param values
     * @return
     */
    @Override
    public List<T> findByCondition(String[] propName, Object[] values) {
        return findByCondition(propName, values, null);
    }

    /**
     * 根据条件查询数据
     *
     * @param propName
     * @param values
     * @param order
     * @return
     */
    @Override
    public List<T> findByCondition(String[] propName, Object[] values, String order) {
        Query query = createQuery(propName, values, order);
        return mongoTemplate.find(query, getEntityClass());
    }

    /**
     * 根据条件查询数据
     *
     * @param criteria
     * @return
     */
    @Override
    public List<T> findByCondition(Criteria criteria) {
        return findByCondition(criteria, null);
    }

    /**
     * 根据条件查询数据
     *
     * @param criteria
     * @param order
     * @return
     */
    @Override
    public List<T> findByCondition(Criteria criteria, String order) {
        List<Order> orderList = parseOrder(order);
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        if (orderList != null && orderList.size() > 0) {
            query.with(Sort.by(orderList));
        }
        return mongoTemplate.find(query, getEntityClass());
    }

    /**
     * 根据条件查询数据
     *
     * @param query
     * @return
     */
    @Override
    public List<T> findByCondition(Query query) {
        return mongoTemplate.find(query, getEntityClass());
    }

    /**
     * 根据条件查询分页数据
     *
     * @param criteria
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public MPage<T> findByPage(Criteria criteria, int pageNo, int pageSize) {
        return findByPage(criteria, null, pageNo, pageSize);
    }

    /**
     * 根据条件查询分页数据
     *
     * @param criteria
     * @param order
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public MPage<T> findByPage(Criteria criteria, String order, int pageNo, int pageSize) {
        List<Order> orderList = parseOrder(order);
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        if (orderList != null && orderList.size() > 0) {
            query.with(Sort.by(orderList));
        }
        return findByPage(query, pageNo, pageSize);
    }

    /**
     * 根据条件查询分页数据
     *
     * @param query
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public MPage<T> findByPage(Query query, int pageNo, int pageSize) {
        long total = this.countByCondition(query);
        MPage<T> page = new MPage<T>(pageNo, pageSize, total);
        query.skip((pageNo - 1) * pageSize).limit(pageSize);
        List<T> rows = this.findByCondition(query);
        page.setList(rows);
        return page;
    }

    /**
     * 根据Id查询记录
     *
     * @param id
     * @return
     */
    @Override
    public T findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, getEntityClass());
    }

    /**
     * 根据主键ID查询数据，只返回一条
     *
     * @param id
     * @return
     */
    @Override
    public T uniqueById(Serializable id) {
        return mongoTemplate.findById(id, getEntityClass());
    }

    /**
     * 根据条件查询数据，只返回一条
     *
     * @param propName
     * @param value
     * @return
     */
    @Override
    public T uniqueByCondition(String propName, Object value) {
        return mongoTemplate.findOne(new Query().addCriteria(Criteria.where(propName).is(value)), getEntityClass());
    }

    /**
     * 根据条件查询数据，只返回一条
     *
     * @param propName
     * @param values
     * @return
     */
    @Override
    public T uniqueByCondition(String[] propName, Object[] values) {
        Query query = createQuery(propName, values, null);
        return mongoTemplate.findOne(query, getEntityClass());
    }

    /**
     * 根据条件查询数据，只返回一条
     *
     * @param criteria
     * @return
     */
    @Override
    public T uniqueByCondition(Criteria criteria) {
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        return mongoTemplate.findOne(query, getEntityClass());
    }

    /**
     * 根据条件查询数据，只返回一条
     *
     * @param query
     * @return
     */
    @Override
    public T uniqueByCondition(Query query) {
        return mongoTemplate.findOne(query, getEntityClass());
    }

    /**
     * 根据条件查询数据，返回数量
     *
     * @param params
     * @param values
     * @return
     */
    @Override
    public long countByCondition(String[] params, Object[] values) {
        Query query = createQuery(params, values, null);
        Long total = mongoTemplate.count(query, getEntityClass());
        return total.longValue();
    }

    /**
     * 根据条件查询数据，返回数量
     *
     * @param criteria
     * @return
     */
    @Override
    public long countByCondition(Criteria criteria) {
        Query query = new Query();
        if (criteria != null) {
            query.addCriteria(criteria);
        }
        Long total = mongoTemplate.count(query, getEntityClass());
        return total.longValue();
    }

    /**
     * 根据条件查询数据，返回数量
     *
     * @param query
     * @return
     */
    @Override
    public long countByCondition(Query query) {
        Long total = mongoTemplate.count(query, getEntityClass());
        return total.longValue();
    }


    @Override
    public void insertAll(Collection<T> collection) {
        mongoTemplate.insertAll(collection);
    }

    /**
     * 初始化修改对象，若有主键ID则返回
     *
     * @param t
     * @param update
     * @param dynamic
     * @return
     */
    protected Query initialUpdateById(T t, Update update, boolean dynamic) {
        try {
            String id = null;
            Object value = null;
            Field[] declaredFields = getEntityClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    id = field.getName();
                    value = field.get(t);
                    break;
                }
            }
            updateHelp(t, update, id, dynamic);
            if (id == null) {
                return null;
            }
            Query query = new Query();
            query.addCriteria(Criteria.where(id).is(value));
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 初始化修改对象，若有主键ID则返回
     *
     * @param t
     * @param update
     * @param dynamic
     * @return
     */
    protected void flushUpdate(T t, Update update, boolean dynamic) {
        try {
            String id = null;
            Field[] declaredFields = getEntityClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Id.class)) {
                    id = field.getName();
                    break;
                }
            }
            updateHelp(t, update, id, dynamic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void flushUpdate(Object t, Update update, boolean dynamic, Class<?> entityClass) {
        try {
            String id = null;
            Field[] declaredFields = entityClass.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Id.class)) {
                    id = field.getName();
                    break;
                }
            }
            updateHelp(t, update, id, dynamic, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改辅助
     *
     * @param t
     * @param update
     * @param id
     * @param dynamic
     */
    private void updateHelp(T t, Update update, String id, boolean dynamic) throws InvocationTargetException, IllegalAccessException {
        Method[] declaredMethods = getEntityClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && method.getModifiers() == Modifier.PUBLIC) {
                String name = methodName.replace("get", "");
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                if (name.equals(id)) {
                    continue;
                }
                Object object = method.invoke(t);
                if (dynamic) {
                    if (object != null) {
                        update.set(name, object);
                    }
                } else {
                    update.set(name, object);
                }
            }
        }
    }

    private void updateHelp(Object t, Update update, String id, boolean dynamic, Class<?> entityClass) throws InvocationTargetException, IllegalAccessException {
        Method[] declaredMethods = entityClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && method.getModifiers() == Modifier.PUBLIC) {
                String name = methodName.replace("get", "");
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                if (name.equals(id)) {
                    continue;
                }
                Object object = method.invoke(t);
                if (dynamic) {
                    if (object != null) {
                        update.set(name, object);
                    }
                } else {
                    update.set(name, object);
                }
            }
        }
    }

    /**
     * 创建查询对象
     *
     * @param propName
     * @param values
     * @param order
     * @return
     */
    private Query createQuery(String[] propName, Object[] values, String order) {
        Query query = new Query();
        if (propName != null && values != null) {
            for (int i = 0; i < propName.length; i++) {
                query.addCriteria(Criteria.where(propName[i]).is(values[i]));
            }
        }
        List<Order> orderList = parseOrder(order);
        if (orderList != null && orderList.size() > 0) {
            query.with(Sort.by(orderList));
        }
        return query;
    }

    /**
     * 解析排序规则
     *
     * @param order
     * @return
     */
    private List<Order> parseOrder(String order) {
        List<Order> list = null;
        if (order != null && !"".equals(order)) {
            list = new ArrayList<>();
            String[] fields = order.split(",");
            Order o = null;
            String[] items = null;
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] == null) {
                    continue;
                }
                items = fields[i].split(" ");
                if (items.length == 1) {
                    o = new Order(Direction.ASC, items[0]);
                } else if (items.length == 2) {
                    o = new Order("desc".equalsIgnoreCase(items[1]) ? Direction.DESC : Direction.ASC, items[0]);
                } else {
                    throw new RuntimeException("order field parse error");
                }
                list.add(o);
            }
        }
        return list;
    }

    /**
     * 获得实现的泛型类型
     *
     * @return
     */
    protected Class<T> getEntityClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

}
