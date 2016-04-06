package org.finra.hiveqlunit.resources.manipulation;

import org.finra.hiveqlunit.resources.TextResource;
import scala.Tuple2;

import java.util.LinkedList;
import java.util.List;

/**
 * A decorative TextResource which can wrap any TextResource
 *
 * Substitutes variables in a TextResources content represented by ${variableName} with a desired value
 * Replaces all such instances of ${variableName} with the desired value
 *
 * Similar to SubstituteVariableResource, but can perform multiple substitutions with one wrapper
 *
 * The wrapped resource is not actually altered, it only looks different to calling code
 */
public class VariableConfigResource implements TextResource {

    private List<Tuple2<String, String>> variableSubstitutions;
    private TextResource baseResource;

    /**
     * Constructs a TextResource that substitutes variables with values in a wrapped TextResource
     *
     * @param baseResource the wrapped resource
     */
    public VariableConfigResource(TextResource baseResource) {
        variableSubstitutions = new LinkedList<>();
        this.baseResource = baseResource;
    }

    /**
     * Constructs a TextResource that substitutes variables with values in a wrapped TextResource
     *
     * Configures using a configuration file, with lines of variableName=value
     *
     * @param configuration the text of a configuration file, best acquired through another TextResource
     * @param baseResource the wrapped resource
     */
    public VariableConfigResource(String configuration, TextResource baseResource) {
        this(baseResource);

        for (String configurationLine : configuration.split("\n|\r\n")) {
            String[] configSplit = configurationLine.split("=");
            addConfig(configSplit[0], configSplit[1]);
        }
    }

    /**
     * Adds additional variable substitutions to the configuration
     *
     * @param variableName the variable to substitute, ie ${variableName} in a script
     * @param variableValue the substitution value
     * @return cascades to makes adding many configurations easy
     */
    public VariableConfigResource addConfig(String variableName, String variableValue) {
        variableSubstitutions.add(new Tuple2<>(variableName, variableValue));
        return this;
    }

    /**
     * Reads the text content of the wrapped TextResource, then substitutes variable instances with the correct values
     *
     * @return the text content of the wrapped TextResource, but with variable substitutions
     */
    @Override
    public String resourceText() {
        String substitutedBase = baseResource.resourceText();
        for (Tuple2<String, String> variableSubstitution : variableSubstitutions) {
            String variableName = variableSubstitution._1();
            String replacementValue = variableSubstitution._2();
            substitutedBase = substitutedBase
                .replaceAll("\\$\\{" + variableName + "\\}", replacementValue);
        }

        return substitutedBase;
    }
}
