package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import org.apache.ibatis.annotations.Param;

/**
 * @desc ${table.comment!} Mapper
 *
 * @author ${author}
 * @version 1.0
 <#if date != "">
 * @date ${date}
 </#if>
 */
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    /**
     * ${table.comment!}批量修改
     <#list table.fields as field>
     <#if field.name?contains('STATUS')>
     * @param ${field.propertyName} 修改状态
     </#if>
     </#list>
     * @param updateBy 更新人
     * @param field 根据指定字段更新 对应数据库字段
     * @param fieldValue 指定字段值
     * @return Void
     */
    void updateStatusByCondition(
    <#list table.fields as field>
        <#if field.name?contains('STATUS')>
            @Param("${field.propertyName}") ${field.propertyType} ${field.propertyName},
        </#if>
    </#list>
            @Param("updateBy") String updateBy,
            @Param("field") String field,
            @Param("fieldValue") Object fieldValue);
}
</#if>