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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class InputStreamResourceTest {

    @Test
    public void readsResourceFromInputStreamTest() {
        InputStreamResource resource = new InputStreamResource() {
            @Override
            public InputStream resourceStream() throws IOException {
                PipedOutputStream outputStream = new PipedOutputStream();
                PipedInputStream inputStream = new PipedInputStream(outputStream);
                PrintStream printer = new PrintStream(outputStream);
                printer.print("Lorem ipsum doler sit amet");
                printer.close();

                return inputStream;
            }
        };

        Assert.assertEquals("Lorem ipsum doler sit amet", resource.resourceText());
    }

    @Test
    public void throwsRunTimeErrorOnExceptionTest() {
        InputStreamResource resource = new InputStreamResource() {
            @Override
            public InputStream resourceStream() throws IOException {
                return new InputStream() {
                    @Override
                    public int read() throws IOException {
                        throw new IOException("The stream is inherently broken");
                    }
                };
            }
        };

        try {
            resource.resourceText();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Failure to load resource", e.getMessage());
        }
    }
}
