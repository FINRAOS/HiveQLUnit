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

import org.junit.Assert;
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

    @Test
    public void commentedLinesWithSemiColons() {
        String testScript = "SELECT foo FROM bar;\n"
            + "-- COMMENT COMMENT COMMENT;\n"
            + "-- COMMENT2 COMMENT2 COMMENT2;\r\n"
            + "SELECT lorem FROM ipsum;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

    @Test
    public void commentsAtEndOfScript() {
        String testScript = "SELECT foo FROM bar;\n"
            + "SELECT lorem FROM ipsum;\n"
            + "-- COMMENT COMMENT COMMENT\n"
            + "-- COMMENT2 COMMENT2 COMMENT2\r\n"
            + "-- COMMENT3 COMMENT3 COMMENT3";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

    @Test
    public void commentsStartsMidLine() {
        String testScript = "SELECT foo --FOO is Important\n"
            + "FROM bar;\n"
            + "SELECT lorem FROM ipsum;\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(2, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[1]);
    }

    @Test
    public void stripsExtraNewLines() {
        String testScript = "SELECT foo FROM bar;\n"
            + "\n"
            + "\r\n"
            + "SELECT bar FROM foo;\r\n"
            + "\r\n"
            + "\n"
            + "SELECT lorem FROM ipsum;\n"
            + "\r\n"
            + "\n";

        String[] statements = ScriptSplitter.splitScriptIntoExpressions(testScript);

        Assert.assertEquals(3, statements.length);
        Assert.assertEquals("SELECT foo FROM bar", statements[0]);
        Assert.assertEquals("SELECT bar FROM foo", statements[1]);
        Assert.assertEquals("SELECT lorem FROM ipsum", statements[2]);
    }

}
