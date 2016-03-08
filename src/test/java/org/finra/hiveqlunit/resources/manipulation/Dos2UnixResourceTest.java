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

import junit.framework.Assert;
import org.finra.hiveqlunit.resources.TextLiteralResource;
import org.finra.hiveqlunit.resources.TextResource;
import org.junit.Test;

public class Dos2UnixResourceTest {

    @Test
    public void lineEndingCorrectionTest() {
        TextResource resource = new Dos2UnixResource(
                new TextLiteralResource("Lorem ipsum doler sit amet\r\nLorem \ripsum doler sit\r\n")
        );

        Assert.assertEquals("Lorem ipsum doler sit amet\nLorem \ripsum doler sit\n", resource.resourceText());
    }
}
