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

package org.finra.hiveqlunit;

import junit.framework.Assert;
import org.apache.spark.sql.Row;
import org.finra.hiveqlunit.resources.TextResource;
import org.finra.hiveqlunit.rules.TestDataLoader;
import org.finra.hiveqlunit.rules.TestHiveServer;
import org.finra.hiveqlunit.script.MultiExpressionScript;
import org.junit.ClassRule;
import org.junit.Rule;

public class HiveQLUnitTest {

    @ClassRule
    public static TestHiveServer hiveServer = new TestHiveServer();

    @Rule
    public static TestDataLoader loader = new TestDataLoader(hiveServer);

    public static void createTable(TextResource tableDDL) {
        new MultiExpressionScript(tableDDL).runScript(hiveServer.getHiveContext());
    }

    public static void loadTableData(String tableName, TextResource tableData) {
        loader.loadDataIntoTable(tableName, tableData);
    }

    public static void loadTableData(String tableName, TextResource tableData, String partitionInfo) {
        loader.loadDataIntoTable(tableName, tableData, partitionInfo);
    }

    public static void runScript(TextResource script) {
        new MultiExpressionScript(script).runScript(hiveServer.getHiveContext());
    }

    public static void  diffActualAndExpected(TextResource diffScript) {
        Row[] rows = new MultiExpressionScript(diffScript).runScriptReturnResults(hiveServer.getHiveContext());
        if (rows.length != 0) {
            Assert.fail();
        }
    }

    public static void  diffActualAndExpected(TextResource diffScript, TextResource tearDownScript) {
        try {
            diffActualAndExpected(diffScript);
        } finally {
            new MultiExpressionScript(tearDownScript).runScript(hiveServer.getHiveContext());
        }
    }
}
