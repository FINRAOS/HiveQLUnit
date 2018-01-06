/*
 * Copyright 2016 HiveQLUnit Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.finra.hiveqlunit.rules;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * TestHiveServer is a TestRule responsible for constructing a HiveContext for use in testing
 * hql scripts. *MUST* be used with the @ClassRule annotation - other HiveQLUnit provided TestRules
 * require this rule to run first. Many classes take a TestHiveServer as an input even though
 * they just need the HiveContext. TestHiveServer serves as a passable reference to the
 * HiveContext before the context has actually been made.
 */
public class TestHiveServer implements TestRule {

    private String serverAddress;
    private ConstructHiveContextStatement constructContext;

    /**
     * Makes a TestHiveServer backed with a local spark instance on one thread.
     */
    public TestHiveServer() {
        this(1);
    }

    /**
     * Makes a TestHiveServer backed with a local spark instance on a requested number of threads.
     *
     * @param localThreadCount the desired number of local threads
     */
    public TestHiveServer(int localThreadCount) {
        serverAddress = "local[" + localThreadCount + "]";
    }

    /**
     * Makes a TestHiveServer backed with a given spark cluster.
     *
     * @param serverAddress the address of the spark cluster
     */
    public TestHiveServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Wraps a given statement with a ConstructHiveContextStatement.
     *
     * @param statement the base statement to be wrapped
     * @param description ignored
     * @return a ConstructHiveContextStatement instance which wraps the provided statement
     */
    @Override
    public Statement apply(Statement statement, Description description) {
        constructContext = new ConstructHiveContextStatement(serverAddress, statement);
        return constructContext;
    }

    /**
     * Provides access to the HiveContext produced by this TestRule.
     *
     * @return the HiveContext produced by this TestRule
     */
    public SQLContext getSqlContext() {
        return constructContext.getSqlContext();
    }

    /**
     * A Statement that performs most of the work for TestHiveServer.
     */
    public static class ConstructHiveContextStatement extends Statement {

        private static SQLContext sparkSqlContextSingleton;
        private String serverAddress;
        private Statement wrappedStatement;

        /**
         * This Statement constructs the all important HiveContext, then evaluates the
         * wrapped Statement.
         *
         * @param serverAddress the address of the backing spark cluster
         * @param wrappedStatement the statement to wrap, which will be evaluated after the
         *                         HiveContext is made
         */
        public ConstructHiveContextStatement(String serverAddress, Statement wrappedStatement) {
            this.serverAddress = serverAddress;
            this.wrappedStatement = wrappedStatement;
        }

        /**
         * Constructs the all important HiveContext, then evaluates the wrapped Statement.
         * Currently, the HiveContext is made as a singleton.
         *
         * @throws Throwable as required by the Statement class
         */
        @Override
        public void evaluate() throws Throwable {
            if (sparkSqlContextSingleton == null) {
                SparkConf sparkConf = new SparkConf().setAppName("HiveQLUnit").setMaster(serverAddress);
                SparkSession sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate();
                sparkSqlContextSingleton = sparkSession.sqlContext();
            }

            wrappedStatement.evaluate();
        }

        /**
         * Provides access to the HiveContext produced by this Statement.
         *
         * @return the HiveContext produced by this TestRule
         */
        public SQLContext getSqlContext() {
            return sparkSqlContextSingleton;
        }
    }
}