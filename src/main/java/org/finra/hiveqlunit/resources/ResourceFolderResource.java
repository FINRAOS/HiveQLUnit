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

import java.io.InputStream;

/**
 * Jars can have files packaged into the resources folder of the jar
 *
 * This TextResource handles access to said resource folder files
 */
public class ResourceFolderResource extends InputStreamResource {

    private String resourcePath;

    /**
     * Constructs a TextResource that reads out the content of a file in the resources folder of a jar
     *
     * @param resourcePath the full path of the resource folder file, ie '/foo/bar/file.dat'
     */
    public ResourceFolderResource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    /**
     * Files in the resource folder are easily accessed as InputStreams
     *
     * @return an InputStream with the contents of the resource folder file
     */
    @Override
    public InputStream resourceStream() {
        return ResourceFolderResource.class.getResourceAsStream(resourcePath);
    }
}
