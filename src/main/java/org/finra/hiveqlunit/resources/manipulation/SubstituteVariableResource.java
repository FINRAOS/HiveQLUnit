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
 * Substitutes variables in a TextResources content represented by ${variableName} with a desired value
 * Replaces all such instances of ${variableName} with the desired value
 *
 * The wrapped resource is not actually altered, it only looks different to calling code
 */
public class SubstituteVariableResource implements TextResource {

    private String variableName;
    private String replacementValue;
    private TextResource baseResource;

    /**
     * Constructs a TextResource that substitutes variables with values in a wrapped TextResource
     *
     * @param variableName the variable name, as seen within ${}, ie ${variableName}
     * @param replacementValue the value to replace ${variableName} with
     * @param baseResource the wrapped TextResource
     */
    public SubstituteVariableResource(String variableName, String replacementValue, TextResource baseResource) {
        this.variableName = variableName;
        this.replacementValue = replacementValue;
        this.baseResource = baseResource;
    }

    /**
     * Reads the text content of the wrapped TextResource, then substitutes variable instances with the correct value
     *
     * @return the text content of the wrapped TextResource, but with variable substitution
     */
    @Override
    public String resourceText() {
        return baseResource.resourceText().replaceAll("\\$\\{" + variableName + "\\}", replacementValue);
    }
}
