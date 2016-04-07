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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Provides an abstract TextResource for building TextResource classes that access InputStream
 * accessible resources. InputStreamResource handles the utility of reading out the content of
 * an InputStream into a String. Child classes provide the InputStream to read from.
 */
public abstract class InputStreamResource implements TextResource {

    /**
     * Provides an InputStream with the text content of the TextResource.
     *
     * @return an InputStream to read the text content of the TextResource from
     * @throws IOException if the InputStream can not be constructed
     */
    public abstract InputStream resourceStream() throws IOException;

    /**
     * Provides the text content of the TextResource by reading out the content of the resource's
     * InputStream.
     *
     * @return the text content of the TextResource, as contained in its provided InputStream
     */
    @Override
    public String resourceText() {
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(resourceStream(), writer, "UTF-8");
            return writer.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException("Failure to load resource");
        }
    }
}
