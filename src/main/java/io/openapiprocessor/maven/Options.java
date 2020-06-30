package io.openapiprocessor.maven;

import org.apache.maven.plugins.annotations.Parameter;

import java.util.Map;

public class Options {

    @Parameter
    private Map<String, Object> values;

    @Parameter
    private Options options;

    public Map<String, Object> getValues () {
        return values;
    }

    public Options getOptions () {
        return options;
    }

}
