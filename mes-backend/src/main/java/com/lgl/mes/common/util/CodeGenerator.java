package com.lgl.mes.common.util;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class CodeGenerator {


    private static StrategyConfig setTable(StrategyConfig strategy) {
        strategy.setTablePrefix("sp_");                               //设置表前缀

        //strategy.setInclude("sp_daily_plan");         //需要包含的表名，允许正则表达式（与exclude(需要排除的表名)二选一配置）
        return strategy;

    }


    /*使用 本代码生成了 product 模块
    * 其它模块是  codegenerateor2 生成的
    * 现在大家都使用 插件生成了， 这个是因为 在这个辣鸡源码上面修改，希望和原来的风格一致
    * */


    // 建议使用这个生成工具
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");            //获取项目路径
        //String  moudlePath ="/com/lgl/mes/line";  //把文件生成到某个模块下面
        gc.setOutputDir(projectPath + "/src/main/java");                //设置生成文件路径
        gc.setAuthor("75039960@qq.com");                                             //设置开发人员
        gc.setOpen(false);                                              //是否打开生成文件
        gc.setSwagger2(true);                                        // 实体属性 Swagger2 注解
        gc.setActiveRecord(true);                                       //开始AR模式
        gc.setFileOverride(true);                                       //是否覆盖已有文件
//        gc.setEnableCache(true);                                      //二级缓存
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://127.0.0.1:3307/hme-mes?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Hongkong&allowPublicKeyRetrieval=true");
        
        // dsc.setSchemaName("public");                             //设置数据库 schema name
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("18665802636");
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("config");        //父包模块名
        pc.setParent("com.lgl.mes");           //父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
        pc.setEntity("entity");                 //设置生成实体类包名
//        pc.setController("controller");
        pc.setMapper("mapper");                    //设置生成mapper包名
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
//        String templatePath = "/templates/mapper.xml.ftl";
//        // 如果模板引擎是 velocity
//        // String templatePath = "/templates/mapper.xml.vm";
//
//        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        String templatePath = "/templates/mapper.xml.ftl";
//        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

//        cfg.setFileCreate(new IFileCreate() {
//            @Override
//            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
//                // 判断自定义文件夹是否需要创建
//                checkDir("调用默认方法创建的目录");
//                return false;
//            }
//        });


        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);                //是否生成XML映射文件
       /* templateConfig.setController(null);         //是否生成controller类
        templateConfig.setService(null);            //是否生成service接口类
        templateConfig.setServiceImpl(null);        //是否生成service1实现类*/

//        templateConfig.setMapper(null);           //是否生成Mapper
        mpg.setTemplate(templateConfig);


        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setSuperEntityClass("com.lgl.mes.common.BaseEntity");
        // strategy.setEntityLombokModel(true);
        //strategy.setRestControllerStyle(true);
        strategy.setRestControllerStyle(false);
        // 公共父类
        strategy.setSuperControllerClass("com.lgl.mes.common.BaseController");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id", "create_time", "create_username", "update_time", "update_username");
        strategy.setInclude("sp_config");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
