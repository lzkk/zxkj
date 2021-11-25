package com.zxkj.common.util;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanUtil {

    public static <T> List<T> copyList(List<?> objList, Class<T> clz) {
        if (objList == null || objList.size() == 0) {
            return null;
        }
        return objList.stream().map(srcObj -> {
            T t = null;
            try {
                t = clz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(srcObj, t);
            return t;
        }).collect(Collectors.toList());
    }

    public static <T> T copyObject(Object srcObj, Class<T> clz) {
        if (srcObj == null) {
            return null;
        }
        T t = null;
        try {
            t = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(srcObj, t);
        return t;
    }
}
