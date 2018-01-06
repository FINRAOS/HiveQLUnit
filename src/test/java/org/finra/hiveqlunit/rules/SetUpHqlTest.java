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

import org.junit.Assert;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.finra.hiveqlunit.script.HqlScript;
import org.junit.Test;
import org.junit.runners.model.Statement;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SetUpHqlTest {

    @Test
    public void setUpHqlRunBeforeStatementTest() {
        final AtomicBoolean scriptRun = new AtomicBoolean(false);
        final AtomicBoolean statementCalled = new AtomicBoolean(false);

        Statement dummyStatement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Assert.assertTrue(scriptRun.get());
                Assert.assertFalse(statementCalled.get());

                statementCalled.set(true);
            }
        };

        TestHiveServer dummyServer = new TestHiveServer() {
            @Override
            public SQLContext getSqlContext() {
                return null;
            }
        };

        HqlScript dummyScript = new HqlScript() {
            @Override
            public void runScript(SQLContext sqlContext) {
                Assert.assertFalse(scriptRun.get());
                Assert.assertFalse(statementCalled.get());

                scriptRun.set(true);
            }

            @Override
            public List<Row> runScriptReturnResults(SQLContext sqlContext) {
                Assert.fail();
                return null;
            }
        };

        SetUpHql setUpHql = new SetUpHql(dummyServer, dummyScript);
        try {
            setUpHql.apply(dummyStatement, null).evaluate();
        } catch (Throwable throwable) {
            Assert.fail();
        }

        Assert.assertTrue(scriptRun.get());
        Assert.assertTrue(statementCalled.get());
    }
}
