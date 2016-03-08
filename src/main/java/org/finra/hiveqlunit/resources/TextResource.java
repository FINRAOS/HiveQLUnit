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
 * Abstracts access to textual resources needed during testing, such as test data or hql scripts
 *
 * By using this interface, code that uses testing resources can be written agnostic to the origin or nature of the resource
 */
public interface TextResource {

    /**
     * Provides the text content of the resource the TextResource object represents
     *
     * @return the text content of the resource the TextResource object represents
     */
    public String resourceText();
}
