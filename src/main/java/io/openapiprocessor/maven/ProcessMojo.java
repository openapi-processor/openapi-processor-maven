/*
 * Copyright 2020 the original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openapiprocessor.maven;

import org.apache.maven.plugin.*;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * run an openapi-processor.
 *
 * @author Martin Hauner
 */
@Mojo (name = "process", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class ProcessMojo extends AbstractMojo {
    public static final String API_PATH = "apiPath";
    public static final String TARGET_DIR = "targetDir";

    @Parameter(required = true)
    private String id;

    @Parameter(required = false)
    private File apiPath;

    @Parameter(required = false)
    private Options options;

    @Parameter(readonly = true, required = true, defaultValue = "${project.basedir}")
    private File baseDir;

    @Override
    public void execute () throws MojoExecutionException {
        String processor = String.format ("openapi-processor-%s", id);

        try {
            getLog().info(String.format ("%10s - %s", "processor", processor));

            Map<String, Object> properties = createProperties ();

            File source = apiPath.getParentFile ();
            String relativeSource = stripBaseDir (source.getAbsolutePath ());
            getLog().info(String.format ("%10s - ${project.basedir}/%s", "apiPath", relativeSource));

            String targetDir = (String) properties.get (TARGET_DIR);
            String relativeTargetDir = stripBaseDir (targetDir);
            getLog().info(String.format ("%10s - ${project.basedir}/%s", "targetDir", relativeTargetDir));

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
        Path base = Paths.get (baseDir.getAbsolutePath ());
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
