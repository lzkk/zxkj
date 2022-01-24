package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @desc ${table.comment!} 服务类
 *
 * @author ${author}
 * @version 1.0
 * @date ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * ${table.comment!}分页列表
     * @param param 根据需要进行传值
     * @return
     */
    IPage<${entity}> page(${entity} param);

    /**
     * ${table.comment!}详情
     * @param id
     * @return
     */
    ${entity} info(Long id);

    /**
     * ${table.comment!}新增
     * @param param 根据需要进行传值
     * @return
     */
    void add(${entity} param);

    /**
     * ${table.comment!}修改
     * @param param 根据需要进行传值
     * @return
     */
    void modify(${entity} param);
}
</#if>