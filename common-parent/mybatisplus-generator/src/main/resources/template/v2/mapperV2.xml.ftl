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
    <#if field.type == 'datetime'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="TIMESTAMP"/>
    <#elseif field.type == 'int'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="INTEGER"/>
    <#elseif field.type != 'datetime' && field.type != 'int'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="${field.type?upper_case}"/>
    </#if>
    </#list>
    <#list table.fields as field>
    <#if !field.keyFlag><#--生成普通字段 -->
    <#if field.type == 'datetime'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="TIMESTAMP"/>
    <#elseif field.type == 'int'>
        <result column="${field.name}" property="${field.propertyName}" jdbcType="INTEGER"/>
    <#elseif field.type != 'datetime' && field.type != 'int'>
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
    <update id="updateStatusByCondition">
        UPDATE ${table.name}
        <set>
        <#list table.fields as field>
        <#if field.name?contains('STATUS')>
          <if test="status != null">
            ${field.name} = ${"#"}{status, ${field.type?upper_case}},
          </if>
        </#if>
        <#if field.name=='UPDATED_BY'>
          <if test="updateBy != null">
            ${field.name} = ${"#"}{updateBy, ${field.type?upper_case}},
          </if>
        </#if>
        <#if field.name=='UPDATED'>
          <if test="'1' == '1'">
            ${field.name} = GETDATE(),
          </if>
        </#if>
        </#list>
        </set>
        WHERE
        ${"$"}{field} = ${"#"}{fieldValue}
    </update>
</mapper>