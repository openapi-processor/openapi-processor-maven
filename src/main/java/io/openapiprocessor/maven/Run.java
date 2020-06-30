package io.openapiprocessor.maven;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

// GENERATE_RESOURCES
@Mojo (name = "oap", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class Run extends AbstractMojo {

    @Override
    public void execute () throws MojoExecutionException, MojoFailureException {
        System.out.println ("openapi-processor!!!");
    }

}
