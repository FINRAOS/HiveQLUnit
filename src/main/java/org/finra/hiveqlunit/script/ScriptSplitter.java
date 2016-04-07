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

/**
 * A static only utility class with functionality to split hql scripts into multiple expressions.
 * Needs some work, the quality of the split it not great; many legal scripts might be parsed
 * wrong.
 */
public final class ScriptSplitter {

    private ScriptSplitter() {
        
    }

    /**
     * Takes an hql script represented as a String and splits it into multiple hql expressions.
     * Expressions are assumed to end with a semi colon followed by a new line (unix style \n
     * or windows style \r\n). Comments and redundant new lines are removed as a preliminary
     * step before splitting.
     *
     * @param script the hql script to split into an expression
     * @return an array of String hql expressions
     */
    public static String[] splitScriptIntoExpressions(String script) {
        String scriptNoExtraLines = script.replaceAll("\n(\n|\r\n)+", "\n");
        String scriptNoComments = scriptNoExtraLines.replaceAll("--.*(\n|\r\n|$)", "");
        return scriptNoComments.split(";\n|;\r\n|;$");
    }
}
