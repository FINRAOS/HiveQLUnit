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

package org.finra.hiveqlunit.resources;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ResourceFolderResourceTest {

    @Test
    public void providesCorrectInputStreamTest() {
        InputStreamResource resource = new ResourceFolderResource("/testResource.txt");
        try {
            InputStream resourceStream = resource.resourceStream();

            int firstByte = resourceStream.read();
            Assert.assertEquals((int) 'A', firstByte);

            int secondByte = resourceStream.read();
            Assert.assertEquals((int) 'B', secondByte);

            int thirdByte = resourceStream.read();
            Assert.assertEquals((int) 'C', thirdByte);
        } catch (IOException e) {
            Assert.fail();
        }
    }

}
