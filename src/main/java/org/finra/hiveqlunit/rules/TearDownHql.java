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

import org.finra.hiveqlunit.script.HqlScript;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A TestRule which executes hive scripts after the test is done as part of a clean up effort
 *
 * The order of evaluation amongst multiple TearDownHql rules is *NON DETERMINISTIC*
 * If evaluation order matters, use the same rule to execute multiple tear down commands as part of the same script
 *
 * If the test method (annotated with @Test) fails, the tear down script will still be run
 */
public class TearDownHql implements TestRule {

    private TestHiveServer testingHiveServer;
    private HqlScript tearDownHql;

    /**
     * Constructs a TearDownHql object
     *
     * @param testingHiveServer the TestHiveServer to run the tear down script on
     * @param tearDownHql a TextResource containing with the tear down script to run
     */
    public TearDownHql(TestHiveServer testingHiveServer, HqlScript tearDownHql) {
        this.testingHiveServer = testingHiveServer;
        this.tearDownHql = tearDownHql;
    }

    /**
     * Wraps a given Statement with a RunTearDownHqlStatement instance
     *
     * When the nested Statement chain is evaluated, the tear down script will run after the wrapped statement is evaluated
     *
     * @param statement the Statement to run the tear down script after evaluation of
     * @param description ignored
     * @return the given Statement wrapped with a RunTearDownHqlStatement
     */
    @Override
    public Statement apply(Statement statement, Description description) {
        return new RunTearDownHqlStatement(statement, testingHiveServer, tearDownHql);
    }

    /**
     * A Statement that does the actual heavy lifting for TearDownHql
     */
    public static class RunTearDownHqlStatement extends Statement {

        private Statement wrappedStatement;
        private TestHiveServer testingHiveServer;
        private HqlScript tearDownHql;

        /**
         * Constructs a RunTearDownHqlStatement
         *
         * When evaluated this Statement evaluates a wrapped statement and then runs an hql script as testing tear down
         *
         * @param wrappedStatement the Statement to execute the tear down script after evaluation of
         * @param testingHiveServer the TestHiveServer to run the tear down script on
         * @param tearDownHql a TextResource containing the hql script to perform tear down with
         */
        public RunTearDownHqlStatement(Statement wrappedStatement, TestHiveServer testingHiveServer, HqlScript tearDownHql) {
            this.wrappedStatement = wrappedStatement;
            this.testingHiveServer = testingHiveServer;
            this.tearDownHql = tearDownHql;
        }

        /**
         * Evaluates the wrapped statement, followed by executing the tear down script
         *
         * The script will always be executed even if a test fails or an error has been thrown
         *
         * @throws Throwable as required by the Statement class
         */
        @Override
        public void evaluate() throws Throwable {
            try {
                wrappedStatement.evaluate();
            } finally {
                tearDownHql.runScript(testingHiveServer.getHiveContext());
            }
        }
    }
}