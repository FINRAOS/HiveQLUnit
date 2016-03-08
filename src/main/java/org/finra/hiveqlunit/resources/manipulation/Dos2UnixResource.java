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

import org.finra.hiveqlunit.resources.TextResource;

/**
 * A decorative TextResource which can wrap any TextResource
 *
 * Replaces \r\n line endings in the wrapped TextResource with \n line endings
 *
 * The wrapped resource is not actually altered, it only looks different to calling code
 */
public class Dos2UnixResource implements TextResource {

    private TextResource baseResource;

    /**
     * Constructs a TextResource that disguises Windows line endings in a wrapped resource with Unix line endings
     *
     * @param baseResource the TextResource to wrap
     */
    public Dos2UnixResource(TextResource baseResource) {
        this.baseResource = baseResource;
    }

    /**
     * Reads the text content of the wrapped TextResource, then changes the line endings of what was read
     *
     * @return the text content of the wrapped TextResource, but with Unix line endings
     */
    @Override
    public String resourceText() {
        return baseResource.resourceText().replaceAll("\r\n", "\n");
    }
}
