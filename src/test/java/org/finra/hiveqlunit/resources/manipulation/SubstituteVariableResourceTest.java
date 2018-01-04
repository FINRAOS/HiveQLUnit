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

package org.finra.hiveqlunit.resources.manipulation;

import org.junit.Assert;
import org.finra.hiveqlunit.resources.TextLiteralResource;
import org.finra.hiveqlunit.resources.TextResource;
import org.junit.Test;

public class SubstituteVariableResourceTest {

    @Test
    public void variableSubstitutionTest() {
        TextResource resource = new SubstituteVariableResource(
                "variable1",
                "value",
                new TextLiteralResource("${variable1}Foo${variable2}Bar${variable1}")
        );

        Assert.assertEquals("valueFoo${variable2}Barvalue", resource.resourceText());
    }
}
