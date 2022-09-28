package com.zxkj.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author yuhui
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        String author = "yuhui";
        // 注释时间（@date），默认 yyyy-MM-dd HH:mm:ss
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //模块名称(不用填写，通过包名来区分)
        String moduleName = "";
        //包名
        String packageName = "com.zxkj.goods.foreign";
        //实体包名 默认:entity
        String entityName = "entity";
        //表名,表名（多个用英文逗号分隔）
//        String tableNames = "BASE_USER,CRM_WS_ORDER";
        String tableNames = "language,product,product_translation";
        generator(author, date, moduleName, packageName, tableNames, entityName);
        System.out.println("执行完成！");
    }

    protected static <T> T configBuilder(IConfigBuilder<T> configBuilder) {
        return null == configBuilder ? null : configBuilder.build();
    }

    private static void generator(String author, String date, String moduleName, String packageName, String tableNames, String entityName) {
        String url = (MybatisPlusGenerator.class.getResource("/").getFile() + "").replace("target/classes/", "src/main/java");
        // 代码生成器
//        new AutoGenerator(configBuilder(new DataSourceConfig.Builder("jdbc:sqlserver://192.168.10.32:1433;DatabaseName=CRM_Retail_Uat", "Retail_Uat", "T4wYGypz#(9::/Mq")))
        new AutoGenerator(configBuilder(new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/fgxm?useUnicode=true&serverTimezone=Asia/Shanghai", "root", "123456")))
                // 全局配置
                .global(configBuilder(new GlobalConfig.Builder()
                        // 覆盖已生成文件，默认 false
                        .fileOverride()
                        // 是否打开生成目录，默认 true
                        .openDir(false)
                        // 输出目录，默认 windows: D://  linux or mac: /tmp
                        .outputDir(url)
                        // 作者，默认无
                        .author(author)
                        // 注释时间（@since），默认 yyyy-MM-dd
                        .commentDate(date)
                        //开启swagger模式
                        .enableSwagger()
                        //日期格式 java.util.date
                        .dateType(DateType.ONLY_DATE)


                ))
                // 包配置
                .packageInfo(configBuilder(new PackageConfig.Builder()
                        // 模块名
                        .moduleName(moduleName)
                        // 父包名
                        .parent(packageName)
                        //实体包名
                        .entity(entityName)
                ))
                // 自定义配置
                .injection(configBuilder(new InjectionConfig.Builder()
                        .beforeOutputFile(new BiConsumer<TableInfo, Map<String, Object>>() {
                            @Override
                            public void accept(TableInfo tableInfo, Map<String, Object> stringObjectMap) {
                                // 自定义 Mapper XML 生成目录
                                ConfigBuilder config = (ConfigBuilder) stringObjectMap.get("config");
                                Map<String, String> pathInfoMap = config.getPathInfo();
                                pathInfoMap.put("xml_path", url + "/" + packageName.replace(".", "/") + "/mapper");
                                stringObjectMap.put("config", config);
                            }
                        })
                ))
                // 策略配置
                .strategy(configBuilder(new StrategyConfig.Builder()
                                // 表名
                                .addInclude(tableNames.split(","))
                                // Entity 策略配置
                                .entityBuilder()
                                // 开启 Lombok 模式
                                .enableLombok()
                                // 开启生成 serialVersionUID
                                .enableSerialVersionUID()
                                // 数据库表映射到实体的命名策略：下划线转驼峰
                                .naming(NamingStrategy.underline_to_camel)
                                // 主键策略为自增，默认 IdType.AUTO
                                .idType(IdType.ASSIGN_ID)

                                // Controller 策略配置
                                .controllerBuilder()
                                // 生成 @RestController 注解
                                .enableRestStyle()
                                // serviceName 去掉I
                                .serviceBuilder().convertServiceFileName((serName) -> "" + serName + ConstVal.SERVICE)
//                        // 父类
//                        .superClass(BaseController.class)
                ))
        // 模板配置
                .template(configBuilder(new TemplateConfig.Builder()
                .entity("/template/entity.java")
                .mapper("/template/mapper.java")
                .mapperXml("/template/mapper.xml")
                .service("/template/service.java", "/template/serviceImpl.java")
                .controller("/template/controller.java")
        ))
                // 执行并指定模板引擎
                .execute(new FreemarkerTemplateEngine());
    }

}