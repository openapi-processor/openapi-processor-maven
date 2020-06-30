package io.openapiprocessor.maven;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;

import java.io.File;
import java.util.Map;

@Mojo (name = "process", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ProcessMojo extends AbstractMojo {

    @Parameter(required = true)
    private File apiPath;

    @Parameter(required = true)
    private String id;

    @Parameter(required = false)
    private Options options;

    @Override
    public void execute () throws MojoExecutionException, MojoFailureException {
        System.out.println ("openapi-processor!!!");
        System.out.println (apiPath);
        System.out.println (id);

        if (options != null) {
            Map<String, Object> values = options.getValues ();

            if (values != null) {
                System.out.println (values.toString ());
            }

            Options nested = this.options.getOptions ();
            if (nested != null) {
                values = nested.getValues ();

                if (values != null) {
                    System.out.println (values.toString ());
                }
            }
        }
    }

}
