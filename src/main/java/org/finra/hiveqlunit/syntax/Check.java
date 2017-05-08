package org.finra.hiveqlunit.syntax;

import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.hive.HiveContext;

import java.io.File;
import java.io.IOException;

/**
 * With a column heder defintion and a sql statement this class can validate the syntax of the sql statement will parse.
 * It takes approximately 20-30 seconds since it starts an intance of hiverserver through spark context.
 */
public class Check {

    HiveContext hc;

    public Check() {

        String header;
        String sql;

        SparkConf sparkConf = new SparkConf().setAppName("HiveQLUnit").setMaster("local[1]");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);


         hc = new HiveContext(sparkContext) ;


        //Blow away hive meta store before execution
        try {
            FileUtils.deleteDirectory(new File("/tmp/foo"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        hc.setConf("hive.metastore.warehouse.dir","/tmp/foo");


    }

    public static boolean verify(String createTableStatement, String sqlStatement ) {

        Boolean isGood = true;

        Check check = new Check();

        check.hc.runSqlHive(createTableStatement);

        check.hc.runSqlHive(sqlStatement);

        try {
            check.hc.runSqlHive(sqlStatement);
        } catch (Exception e) {
            e.printStackTrace();
            isGood = false;
        }

        return isGood;
    }

}
