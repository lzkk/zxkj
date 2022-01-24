package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhgd.common.user.util.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @desc ${table.comment!} 服务实现类
 *
 * @author ${author}
 * @version 1.0
 * @date ${date}
 */
@Service
@Slf4j
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Autowired
    private PageUtils pageUtils;

    /**
     * ${table.comment!}分页列表
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    public IPage<${entity}> page(${entity} param) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
        <#list table.fields as field>
            // ${field.comment}
            <#if !entityLombokModel>
                <#if field.propertyType == "Boolean">
                    <#assign getprefix="is"/>
                <#else>
                    <#assign getprefix="get"/>
                </#if>
                <#if field.propertyType == "String">
                    .eq(!StringUtils.isEmpty(param.${getprefix}${field.capitalName}()), ${entity}::${getprefix}${field.capitalName}, param.${getprefix}${field.capitalName}())
                <#else>
                    .eq(param.${getprefix}${field.capitalName}() != null, ${entity}::${getprefix}${field.capitalName}, param.${getprefix}${field.capitalName}())
                </#if>
            <#else>
                <#if field.propertyType == "String">
                    .eq(!StringUtils.isEmpty(param.get${field.capitalName}()), ${entity}::get${field.capitalName}, param.get${field.capitalName}())
                <#else>
                    .eq(param.get${field.capitalName}() != null, ${entity}::get${field.capitalName}, param.get${field.capitalName}())
                </#if>
            </#if>
        </#list>;
        IPage<${entity}> page = page(pageUtils.page(), queryWrapper);
        return page;
    }

    /**
     * ${table.comment!}详情
     * @param id
     * @return
     */
    @Override
    public ${entity} info(Long id) {
        return getById(id);
    }

    /**
     * ${table.comment!}新增
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    @Transactional
    public void add(${entity} param) {
        save(param);
    }

    /**
     * ${table.comment!}修改
     * @param param 根据需要进行传值
     * @return
     */
    @Override
    @Transactional
    public void modify(${entity} param) {
        updateById(param);
    }
}
</#if>