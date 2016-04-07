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

/**
 * The most basic TextResource - wraps text literals or Java strings as a TextResource.
 */
public class TextLiteralResource implements TextResource {

    private String resourceText;

    /**
     * Constructs a TextResource that wraps a string value, which is the resources' text content.
     *
     * @param resourceText the resources' text content
     */
    public TextLiteralResource(String resourceText) {
        this.resourceText = resourceText;
    }

    /**
     * This TextResource wraps a string value, which is the resource's text content.
     *
     * @return the resourceText param passed to the constructor
     */
    @Override
    public String resourceText() {
        return resourceText;
    }
}
