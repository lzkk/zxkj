package com.zxkj.common.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author ：yuhui
 * @date ：Created in 2021/11/10 18:25
 */
public class ClassScanUtil {

    /**
     * 从指定包下扫码所有符合类对象条件的类
     *
     * @param packageResourcePattern
     * @param annotationClass
     * @return
     */
    public static Set<Class<?>> scan(String packageResourcePattern, Class<? extends Annotation> annotationClass) {
        Set<Class<?>> classSet = new HashSet<>();
        List<AnnotationTypeFilter> typeFilters = new ArrayList<>();
        typeFilters.add(new AnnotationTypeFilter(annotationClass, false));
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resourcePatternResolver.getResources(packageResourcePattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader reader = readerFactory.getMetadataReader(resource);
                    String className = reader.getClassMetadata().getClassName();
                    if (matchesEntityTypeFilter(reader, readerFactory, typeFilters)) {
                        Class<?> curClass = Thread.currentThread().getContextClassLoader().loadClass(className);
                        classSet.add(curClass);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classSet;
    }

    /**
     * 检查当前扫描到的Bean含有任何一个指定的注解标记
     *
     * @param reader
     * @param readerFactory
     * @return
     * @throws IOException
     */
    private static boolean matchesEntityTypeFilter(MetadataReader reader, MetadataReaderFactory readerFactory, List<AnnotationTypeFilter> typeFilters) throws IOException {
        if (typeFilters.isEmpty()) {
            return false;
        }
        for (AnnotationTypeFilter filter : typeFilters) {
            if (filter.match(reader, readerFactory)) {
                return true;
            }
        }
        return false;
    }

}
