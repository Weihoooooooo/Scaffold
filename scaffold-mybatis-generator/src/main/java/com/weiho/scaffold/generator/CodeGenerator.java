package com.weiho.scaffold.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.weiho.scaffold.mp.entity.CommonEntity;
import com.weiho.scaffold.mp.mapper.CommonMapper;
import com.weiho.scaffold.mp.service.CommonService;
import com.weiho.scaffold.mp.service.impl.CommonServiceImpl;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("all")
public class CodeGenerator {
    //MP代码生成器
    public static void main(String[] args) {
        //数据库连接url
        String databaseUrl = "jdbc:mysql://192.168.137.128:3306/scaffold?" +
                "serverTimezone=Asia/Shanghai&characterEncoding=utf8" +
                "&useSSL=false&allowPublicKeyRetrieval=true";
        //数据库连接用户名
        String databaseUsername = "root";
        //数据库连接密码
        String databasePassword = "970049938";
        //Java模块名称
        String javaModuleName = "scaffold-system";
        //生成Java doc的作者名字
        String javaDocAuthorName = "Weiho";
        //父包名
        String parentPackageName = "com.weiho.scaffold.system";
        //要创建系列文件的表名
        String[] tableNames = {"owner"};
        //若要设置Entity、Service、ServiceImpl、Controller的父类滑动下去修改


        //数据库配置
        FastAutoGenerator.create(new DataSourceConfig.Builder(
                        databaseUrl,
                        databaseUsername,
                        databasePassword
                ).dbQuery(new MySqlQuery())
        ).globalConfig(builder -> {//全局配置
            builder.outputDir(System.getProperty("user.dir") + (javaModuleName.isEmpty() ? "" : "/" + javaModuleName) + "/src/main/java")//输出路径
                    .disableOpenDir()//禁止打开输出目录
                    .author(javaDocAuthorName)//作者
                    .enableSwagger()//开启Swagger模式
                    .dateType(DateType.ONLY_DATE)//时间策略
                    .commentDate("yyyy-MM-dd");//注释日期
        }).packageConfig(builder -> { //包配置
            builder.parent(parentPackageName)//父包名
//                    .moduleName("untitled")//模块名称
                    .xml("mapper")//Mapper XML 包名
                    //修改Mapper.xml到resources文件夹中的mapper文件夹中
                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + (javaModuleName.isEmpty() ? "" : "/" + javaModuleName) + "/src/main/resources/mapper/system/"));
        }).strategyConfig(builder -> {//策略配置
            builder.addInclude(Arrays.asList(tableNames))//增加表匹配
                    //entity策略
                    .entityBuilder()
                    .superClass(CommonEntity.class)//设置 entity 父类 (可以写类.class,也可以全包"com.abc.XX")
                    .addSuperEntityColumns("create_time", "update_time", "is_del")//添加父类公共字段
                    .disableSerialVersionUID()//禁用生成 serialVersionUID
                    .enableLombok()//开启 lombok 模型
                    .enableTableFieldAnnotation()//开启生成实体时生成字段注解
                    .logicDeleteColumnName("is_del")//逻辑删除字段名(数据库)
                    .logicDeletePropertyName("isDel")//逻辑删除属性名(实体)
                    .naming(NamingStrategy.underline_to_camel)//表名转换方式: 数据库中的下划线转成java驼峰
                    .columnNaming(NamingStrategy.underline_to_camel)//列名转换
                    .idType(IdType.AUTO)//全局主键类型,为数据库中的自增

                    //controller策略
                    .controllerBuilder()
//                    .superClass("XXX")//设置 controller 父类 (可以写类.class,也可以全包"com.abc.XX")
                    .enableHyphenStyle()//开启驼峰转连字符 默认是false
                    .enableRestStyle()//开启生成@RestController 控制器
                    .formatFileName("%sController")//格式化 controller 文件名称

                    //service策略
                    .serviceBuilder()
                    .superServiceClass(CommonService.class)//设置 service 接口父类 (可以写类.class,也可以全包"com.abc.XX") 没有默认继承 IService
                    .superServiceImplClass(CommonServiceImpl.class)//设置 service 实现类父类 (可以写类.class,也可以全包"com.abc.XX") 没有默认继承 ServiceImpl
                    .formatServiceFileName("%sService")//格式化 service 文件名称
                    .formatServiceImplFileName("%sServiceImpl")//格式化 serviceImpl 文件名称

                    //mapper策略
                    .mapperBuilder()
                    .superClass(CommonMapper.class)//设置 mapper 接口父类 (可以写类.class,也可以全包"com.abc.XX") 没有默认继承 BaseMapper
                    .enableMapperAnnotation()//开启@Mapper注解
                    .enableBaseResultMap()//生成 Mapper.xml 中 BaseResultMap
                    .enableBaseColumnList()//生成 Mapper.xml 中 BaseColumnList
                    .formatMapperFileName("%sMapper")//格式化 mapper 文件名称
                    .formatXmlFileName("%sMapper");//格式化 xml 文件名称

        }).templateEngine(new FreemarkerTemplateEngine()).execute();
    }
}
