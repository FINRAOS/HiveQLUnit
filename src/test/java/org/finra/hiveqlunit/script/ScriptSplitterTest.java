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

import junit.framework.Assert;
import org.junit.Test;

public class ScriptSplitterTest {

    @Test
    public void unixStyleLineEndings() {
        String testScript = "SELECT foo FROM bar;\n"
                + "SELECT lorem FROM ipsum;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

    @Test
    public void unixEndingInsideExpressionIgnored() {
        String testScript = "SELECT foo\n FROM bar;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(1, statements.length);
        Assert.assertEquals("SELECT foo\n FROM bar", statements[0]);
    }

    @Test
    public void windowsStyleLineEndings() {
        String testScript = "SELECT foo FROM bar;\r\n"
                + "SELECT lorem FROM ipsum;\r\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

    @Test
    public void windowsEndingInsideExpressionIgnored() {
        String testScript = "SELECT foo\r\n FROM bar;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(1, statements.length);
        Assert.assertEquals("SELECT foo\r\n FROM bar", statements[0]);
    }

    @Test
    public void terminalLineEndingOptional() {
        String testScript = "SELECT foo FROM bar;\n"
                + "SELECT lorem FROM ipsum;";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

    @Test
    public void trailingSemiColonNotPartOfScript() {
        String testScript = "SELECT foo FROM bar;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(1, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
    }

    @Test
    public void excludesCommentedLines() {
        String testScript = "SELECT foo FROM bar;\n"
                + "-- COMMENT COMMENT COMMENT\n"
                + "-- COMMENT2 COMMENT2 COMMENT2\r\n"
                + "SELECT lorem FROM ipsum;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

}
