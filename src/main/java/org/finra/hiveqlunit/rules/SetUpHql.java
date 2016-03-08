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
 * A TestRule which executes hive scripts before the test is executed as part of a set up effort
 *
 * The order of evaluation amongst multiple SetUpHql rules is *NON DETERMINISTIC*
 * If evaluation order matters, use the same rule to execute multiple set up commands as part of the same script
 */
public class SetUpHql implements TestRule {

    private TestHiveServer testingHiveServer;
    private HqlScript setUpHql;

    /**
     * Constructs a SetUpHql object
     *
     * @param testingHiveServer the TestHiveServer to run the set up on
     * @param setUpHql a TextResource containing with the set up script to run
     */
    public SetUpHql(TestHiveServer testingHiveServer, HqlScript setUpHql) {
        this.testingHiveServer = testingHiveServer;
        this.setUpHql = setUpHql;
    }

    /**
     * Wraps a given Statement with a RunSetUpHqlStatement instance
     *
     * When the nested Statement chain is evaluated, set up script is run, and then the wrapped Statement is evaluated
     *
     * @param statement the Statement to run the set up script before evaluation of
     * @param description ignored
     * @return the given Statement wrapped with a RunSetUpHqlStatement
     */
    @Override
    public Statement apply(Statement statement, Description description) {
        return new RunSetUpHqlStatement(statement, testingHiveServer, setUpHql);
    }

    /**
     * A Statement that does the actual heavy lifting for SetUpHql
     */
    public static class RunSetUpHqlStatement extends Statement {

        private Statement wrappedStatement;
        private TestHiveServer testingHiveServer;
        private HqlScript setUpHql;

        /**
         * Constructs a RunSetUpHqlStatement
         *
         * When evaluated this Statement runs a hive script as set up code and then evaluates a wrapped Statement
         *
         * @param wrappedStatement the Statement to execute the set up script before evaluation of
         * @param testingHiveServer the TestHiveServer to run the set up script on
         * @param setUpHql a TextResource containing the hql script to perform set up with
         */
        public RunSetUpHqlStatement(Statement wrappedStatement, TestHiveServer testingHiveServer, HqlScript setUpHql) {
            this.wrappedStatement = wrappedStatement;
            this.testingHiveServer = testingHiveServer;
            this.setUpHql = setUpHql;
        }

        /**
         * Executes the set up hql script, and then evaluates the wrapped Statement
         *
         * @throws Throwable as required by the Statement class
         */
        @Override
        public void evaluate() throws Throwable {
            setUpHql.runScript(testingHiveServer.getHiveContext());
            wrappedStatement.evaluate();
        }
    }
}
