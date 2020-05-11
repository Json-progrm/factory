import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

/**
 * @author Json
 * @date 2020-03-17:27
 */
public class MybatisAuto {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        GlobalConfig gc = new GlobalConfig();

        //1、当前的项目路径
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");

        gc.setAuthor("Json");
        gc.setOpen(false);
        gc.setFileOverride(false);//是否覆盖以前的项目结构
        gc.setServiceName("%sService");//去掉service的 “ I ”前缀
        gc.setIdType(IdType.AUTO);//主键生成策略
        gc.setDateType(DateType.ONLY_DATE);//日期格式
        gc.setBaseResultMap(true);// 是否生成BaseResultMap
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);


        //2、数据库配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://cdb-kp1doq8w.cd.tencentcdb.com:10081/admin?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("fblj112358");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        //3、包名配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName("");//设置模块名
        pc.setParent("com.getheart");
        pc.setEntity("pojo");
        pc.setService("service");
        pc.setMapper("mapper");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("sys_role");//设置需要映射的表名
        strategy.setNaming(NamingStrategy.underline_to_camel); //数据库表映射到实体类的命名策略：下划线转陀峰
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategy.setEntityLombokModel(true);//自动生成lombok
        strategy.setRestControllerStyle(true);//是否生成restController
        strategy.setControllerMappingHyphenStyle(true);//  localhost:8080/hello_id_2
        strategy.setTablePrefix("sys_");

        //逻辑删除
        strategy.setLogicDeleteFieldName("deleted");
        //乐观锁配置
        strategy.setVersionFieldName("version");

        //自动填充
        TableFill createTimeime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTimeime = new TableFill("update_time", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTimeime);
        tableFills.add(updateTimeime);
        strategy.setTableFillList(tableFills);

        mpg.setStrategy(strategy);

        mpg.execute();//执行
    }
}
