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
import org.finra.hiveqlunit.resources.TextResource;

import java.util.List;

/**
 * Runs a single hql expression, with no heed for comments or scripts with multiple expressions
 * in them.
 */
public class SingleExpressionScript implements HqlScript {

    private String expression;

    /**
     * The provided hql script will be run as a single expression with no pre-processing.
     *
     * @param expressionResource a TextResource, properly containing one hql expression with no comments or such
     */
    public SingleExpressionScript(TextResource expressionResource) {
        expression = expressionResource.resourceText();
    }

    /**
     * Runs the hql contained in the constructor given TextResource, treating it as a single
     * expression with no comments.
     *
     * @param sqlContext an SQLContext, as provided by spark through the TestHiveServer TestRule, used to run hql expressions
     */
    @Override
    public void runScript(SQLContext sqlContext) {
        sqlContext.sql(expression);
    }

    /**
     * Runs the hql contained in the constructor given TextResource, treating it as a single
     * expression with no comments.
     *
     * @param sqlContext an SQLContext, as provided by spark through the TestHiveServer TestRule, used to run hql expressions
     * @return a result set of Rows produced by running the hql script or expressions represented by this HqlScript
     */
    @Override
    public List<Row> runScriptReturnResults(SQLContext sqlContext) {
        return sqlContext.sql(expression).collectAsList();
    }
}
