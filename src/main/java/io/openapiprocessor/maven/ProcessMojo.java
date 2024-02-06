/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-maven
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * run an openapi-processor.
 */
@Mojo (name = "process", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ProcessMojo extends AbstractMojo {
    public static final String API_PATH = "apiPath";
    public static final String TARGET_DIR = "targetDir";

    @Parameter(required = true)
    private String id;

    @Parameter(required = true)
    private File apiPath;

    @Parameter(required = false)
    private Options options;

    @Parameter(required = false, defaultValue = "true")
    private boolean addSourceRoot;

    @Parameter(readonly = true, required = true, defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute () throws MojoExecutionException {
        String processor = String.format ("openapi-processor-%s", id);

        try {
            getLog().info(String.format ("%10s - %s", "processor", processor));

            Map<String, Object> properties = createProperties ();

            File source = apiPath.getParentFile ();
            String relativeSource = stripBaseDir (source.getAbsolutePath ());
            getLog().info(String.format ("%10s - ${project.basedir}%s%s", "apiPath", File.separator, relativeSource));

            String targetDir = (String) properties.computeIfAbsent (TARGET_DIR, k -> project.getBuild().getDirectory() + File.separator + "generated-sources" + File.separator + id);

            if (addSourceRoot) {
                project.addCompileSourceRoot(targetDir);
            }

            String relativeTargetDir = stripBaseDir (targetDir);
            getLog().info(String.format ("%10s - ${project.basedir}%s%s", "targetDir", File.separator, relativeTargetDir));

            File targetRoot = new File(targetDir);
            UpToDateCheck upToDateCheck = new UpToDateCheck ();
            boolean upToDate = upToDateCheck.isUpToDate (source, targetRoot);

            if (!upToDate) {
                getLog().info ("");
                getLog().info( "Changes detected - generating target files!" );

                new ProcessorRunner (id, properties)
                    .run ();

            } else {
                getLog().info ("");
                getLog().info( "Nothing to process - all generated target files are up to date." );
            }

        } catch (Exception e) {
            throw new MojoExecutionException (String.format("Execution failed - %s", processor), e);
        }
    }

    private String stripBaseDir (String source) {
        Path base = Paths.get (project.getBasedir().getAbsolutePath ());
        Path src = Paths.get (source);
        return base.relativize (src).toString ();
    }

    private Map<String, Object> createProperties () throws MojoExecutionException {
        Map<String, Object> properties = new HashMap<> ();

        addProperties (options, properties);
        setApiPath (properties);

        return properties;
    }

    private void addProperties (Options options, Map<String, Object> target) {
        if (options == null)
            return;

        Map<String, Object> source = options.getValues ();
        if (source == null)
            return;

        target.putAll (source);

        addNestedProperties (options.getNested (), target);
    }

    private void addNestedProperties (Nested nested, Map<String, Object> parent) {
        if (nested == null)
            return;

        Map<String, Object> source = nested.getValues ();
        if (source == null)
            return;

        Map<String, Object> target = new HashMap<> (source);
        parent.put (nested.getName (), target);

        addNestedProperties (nested.getNested (), target);
    }

    // copy common api path to openapi-processor props if not set
    private void setApiPath (Map<String, Object> properties) throws MojoExecutionException {
        if (!properties.containsKey (API_PATH)) {
            if (apiPath == null) {
                throw new MojoExecutionException (this,
                    "'common <apiPath>' or '" + id + " <apiPath>' not set!",
                    "'common <apiPath>' or '" + id + " <apiPath>' not set!");
            }

            properties.put (API_PATH, apiPath);
        } else {
            apiPath = new File((String) properties.get (API_PATH));
        }
   }

}
