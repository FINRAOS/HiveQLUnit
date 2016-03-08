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

import junit.framework.Assert;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;
import org.finra.hiveqlunit.script.HqlScript;
import org.junit.Test;
import org.junit.runners.model.Statement;

import java.util.concurrent.atomic.AtomicBoolean;

public class TearDownHqlTest {

    @Test
    public void tearDownHqlRunAfterStatement() {
        final AtomicBoolean scriptRun = new AtomicBoolean(false);
        final AtomicBoolean statementCalled = new AtomicBoolean(false);

        Statement dummyStatement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Assert.assertFalse(scriptRun.get());
                Assert.assertFalse(statementCalled.get());

                statementCalled.set(true);
            }
        };

        TestHiveServer dummyServer = new TestHiveServer() {
            @Override
            public HiveContext getHiveContext() {
                return null;
            }
        };

        HqlScript dummyScript = new HqlScript() {
            @Override
            public void runScript(HiveContext hqlContext) {
                Assert.assertFalse(scriptRun.get());
                Assert.assertTrue(statementCalled.get());

                scriptRun.set(true);
            }

            @Override
            public Row[] runScriptReturnResults(HiveContext hqlContext) {
                Assert.fail();
                return null;
            }
        };

        TearDownHql tearDownHql = new TearDownHql(dummyServer, dummyScript);
        try {
            tearDownHql.apply(dummyStatement, null).evaluate();
        } catch (Throwable throwable) {
            Assert.fail();
        }

        Assert.assertTrue(scriptRun.get());
        Assert.assertTrue(statementCalled.get());
    }
}
