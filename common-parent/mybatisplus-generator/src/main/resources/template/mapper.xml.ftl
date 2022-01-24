<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
    <#list table.fields as field>
    <#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" jdbcType="${field.type?upper_case}"/>
    </#if>
    </#list>
    <#list table.commonFields as field><#--生成公共字段 -->
    <#if field.type == 'datetime' || field.type == 'datetime2'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="TIMESTAMP"/>
    <#elseif field.type == 'int'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="INTEGER"/>
    <#elseif field.type != 'datetime' && field.type != 'int' && field.type != 'datetime2'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="${field.type?upper_case}"/>
    </#if>
    </#list>
    <#list table.fields as field>
    <#if !field.keyFlag><#--生成普通字段 -->
    <#if field.type == 'datetime' || field.type == 'datetime2'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="TIMESTAMP"/>
    <#elseif field.type == 'int'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="INTEGER"/>
    <#elseif field.type != 'datetime' && field.type != 'int' && field.type != 'datetime2'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="${field.type?upper_case}"/>
    </#if>
    </#if>
    </#list>
    </resultMap>
    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        <#list table.commonFields as field>
            ${field.name},
        </#list>
        ${table.fieldNames}
    </sql>
    <insert id="insertAll" parameterType="${package.Entity}.${entity}" >
        insert into ${table.name}
        <trim prefix="(" suffix=")" suffixOverrides="," >
        <#list table.fields as field>
            <#if !field.keyFlag>
            ${field.name},
            </#if>
        </#list>
        </trim>
        VALUES
        <foreach collection="paramList" separator="," item="param" index="index">
            <trim prefix="(" suffix=")" suffixOverrides="," >
            <#list table.fields as field>
            <#if field.name!='ID'>
            <#if field.type == 'datetime' || field.type == 'datetime2'>
                ${"#"}{param.${field.propertyName}, jdbcType=TIMESTAMP},
            <#elseif field.type == 'int'>
                ${"#"}{param.${field.propertyName}, jdbcType=INTEGER},
            <#elseif field.type != 'datetime' && field.type != 'int' && field.type != 'datetime2'>
                ${"#"}{param.${field.propertyName}, jdbcType=${field.type?upper_case}},
            </#if>
            </#if>
            </#list>
            </trim>
        </foreach>
    </insert>
</mapper>