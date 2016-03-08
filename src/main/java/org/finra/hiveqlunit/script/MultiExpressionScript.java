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
import org.apache.spark.sql.hive.HiveContext;
import org.finra.hiveqlunit.resources.TextResource;

/**
 * Runs an hql script containing multiple expressions, using the ScriptSplitter utility to derive individual expressions
 */
public class MultiExpressionScript implements HqlScript {

    private String script;

    /**
     * Constructs a MultiExpressionScript
     *
     * @param expressionResource a TextResource containing an hql script for MultiExpressionScript to run
     */
    public MultiExpressionScript(TextResource expressionResource) {
        script = expressionResource.resourceText();
    }

    /**
     * Splits the bundled hql script into multiple expressions using ScriptSlitter utility class
     *
     * Each expression is run on the provided HiveContext
     *
     * @param hqlContext an HqlContext, as provided by spark through the TestHiveServer TestRule, used to run hql expressions
     */
    @Override
    public void runScript(HiveContext hqlContext) {
        String[] expressions = ScriptSplitter.splitScriptIntoExpressions(script);
        for (String expression : expressions) {
            hqlContext.sql(expression);
        }
    }

    /**
     * Splits the bundled hql script into multiple expressions using ScriptSlitter utility class
     *
     * Each expression is run on the provided HiveContext
     *
     * @param hqlContext an HqlContext, as provided by spark through the TestHiveServer TestRule, used to run hql expressions
     * @return the row results acquired from the last executed expression
     */
    @Override
    public Row[] runScriptReturnResults(HiveContext hqlContext) {
        String[] expressions = ScriptSplitter.splitScriptIntoExpressions(script);
        for (int i = 0; i < expressions.length - 1; i++) {
            String expression = expressions[i];
            hqlContext.sql(expression);
        }
        return hqlContext.sql(expressions[expressions.length - 1]).collect();
    }
}
