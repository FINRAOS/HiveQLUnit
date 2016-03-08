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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Handles access to text resources contained in files on the local file system
 */
public class LocalFileResource extends InputStreamResource {

    private String filePath;

    /**
     * Constructs a TextResource that reads out the content of a file on the local file system
     *
     * @param filePath the file system path of the file
     */
    public LocalFileResource(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Files ion the local file system are easily accessed as InputStreams
     *
     * @return an InputStream with the contents of the local file
     */
    @Override
    public InputStream resourceStream() throws IOException{
        return new FileInputStream(new File(filePath));
    }
}
