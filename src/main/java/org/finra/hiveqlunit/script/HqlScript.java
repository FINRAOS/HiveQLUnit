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

package org.finra.hiveqlunit.script;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

/**
 * Abstracts the execution of hql scripts or expressions. How to handle comments in scripts,
 * or split scripts into runnable expressions is handled here. This is separate from acquiring
 * the text representing the hql script or expression, TextResources better handle that.
 */
public interface HqlScript {

    /**
     * Runs the sql script or expressions represented by this HqlScript using a HiveContext.
     *
     * @param sqlContext an SQLContext, as provided by spark through the TestHiveServer TestRule, used to run hql expressions
     */
    public void runScript(SQLContext sqlContext);

    /**
     * Runs the hql script or expressions represented by this HqlScript using a HiveContext,
     * returning a results set from the script.
     *
     * @param sqlContext an SQLContext, as provided by spark through the TestHiveServer TestRule, used to run hql expressions
     * @return a result set of Rows produced by running the hql script or expressions represented by this HqlScript
     */
    public List<Row> runScriptReturnResults(SQLContext sqlContext);

}
