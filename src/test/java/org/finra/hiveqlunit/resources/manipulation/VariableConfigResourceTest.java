package org.finra.hiveqlunit.resources.manipulation;

import org.junit.Assert;
import org.finra.hiveqlunit.resources.TextLiteralResource;
import org.finra.hiveqlunit.resources.TextResource;
import org.junit.Test;

public class VariableConfigResourceTest {

    @Test
    public void substitutionsTest() {
        TextResource baseResource = new TextLiteralResource(
            "${variable1} ${variable2} ${variable3} ${variable2}");

        VariableConfigResource configured = new VariableConfigResource(baseResource)
            .addConfig("variable1", "foo")
            .addConfig("variable2", "bar")
            .addConfig("variable3", "lorem");

        Assert.assertEquals("foo bar lorem bar", configured.resourceText());
    }

    @Test
    public void configureFileConstructorTest() {
        String config = "variable1=foo\n"
            + "variable2=bar\r\n"
            + "variable3=lorem";

        TextResource baseResource = new TextLiteralResource(
            "${variable1} ${variable2} ${variable3} ${variable2}");

        VariableConfigResource configured = new VariableConfigResource(config, baseResource);
        Assert.assertEquals("foo bar lorem bar", configured.resourceText());
    }

}
