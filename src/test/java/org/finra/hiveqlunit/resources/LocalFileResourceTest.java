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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class LocalFileResourceTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void providesCorrectInputStreamTest() {
        try {
            File testData = testFolder.newFile("testData.txt");
            OutputStream fileOutput = new FileOutputStream(testData);
            PrintStream printStream = new PrintStream(fileOutput);
            printStream.print("ABC");
            printStream.close();

            InputStreamResource fileResource = new LocalFileResource(testData.getAbsolutePath());
            InputStream resourceStream = fileResource.resourceStream();

            int firstByte = resourceStream.read();
            org.junit.Assert.assertEquals((int) 'A', firstByte);

            int secondByte = resourceStream.read();
            org.junit.Assert.assertEquals((int) 'B', secondByte);

            int thirdByte = resourceStream.read();
            org.junit.Assert.assertEquals((int) 'C', thirdByte);
        } catch (IOException e) {
            Assert.fail();
        }
    }
}
