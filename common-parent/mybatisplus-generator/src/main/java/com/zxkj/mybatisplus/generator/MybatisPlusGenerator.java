package com.zxkj.mybatisplus.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;

/**
 * @author yuhui
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {
        String author = "yuhui";
        // 注释时间（@date），默认 yyyy-MM-dd HH:mm:ss
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //模块名称
        String moduleName = "";
        //包名
        String packageName = "com.zxkj.order";
        //表名  表名（英文逗号分隔）
        String tableNames = "order_info";
        generator(author, date, moduleName, packageName, tableNames);
        System.out.println("执行完成！");
    }

    /**
     * 读取控制台输入内容
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 控制台输入内容读取并打印提示信息
     *
     * @param message 提示信息
     * @return
     */
    public static String scannerNext(String message) {
        System.out.println(message);
        String nextLine = scanner.nextLine();
        if (StringUtils.isBlank(nextLine)) {
            // 如果输入空行继续等待
            return scanner.next();
        }
        return nextLine;
    }

    protected static <T> T configBuilder(IConfigBuilder<T> configBuilder) {
        return null == configBuilder ? null : configBuilder.build();
    }

    private static void generator(String author, String date, String moduleName, String packageName, String tableNames) {
        String url = (MybatisPlusGenerator.class.getResource("/").getFile() + "").replace("target/classes/", "src/main/java");
        // 代码生成器
        new AutoGenerator(configBuilder(new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/shop_order?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", "root", "123456")))
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