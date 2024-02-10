/*
 * Copyright 2020 https://github.com/openapi-processor/openapi-processor-maven
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.maven;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

import java.io.File;

import static com.soebes.itf.extension.assertj.MavenITAssertions.assertThat;

@MavenJupiterExtension
public class MojoIT {

    @MavenTest
    @MavenGoal("openapi-processor:process")
    void writesOutputToExplicitTargetDir(MavenExecutionResult result) {
        String targetDir = String.join(File.separator, "target", "generated-sources", "expected-target-dir");
        String packageDir = String.join(File.separator, targetDir, "io", "openapiprocessor", "openapi");
        String apiPackage = String.join(File.separator, packageDir, "api");
        String modelPackage = String.join(File.separator, packageDir, "model");
        String supportPackage = String.join(File.separator, packageDir, "support");

        assertThat(result)
                .isSuccessful()
                .project()
                .has(targetDir)
                .has(packageDir)
                .has(apiPackage)
                .has(modelPackage)
                .has(supportPackage);

        assertThat(result)
            .out()
            .info()
            .contains("   apiPath - " + joinDirs("${project.basedir}", "src", "api", "openapi.yaml"))
            .contains(" targetDir - " + joinDirs("${project.basedir}", "target", "generated-sources", "expected-target-dir"));
    }

    @MavenTest
    @MavenGoal("openapi-processor:process")
    void writesOutputToAutomaticTargetDir(MavenExecutionResult result) {
        String targetDir = String.join(File.separator, "target", "generated-sources", "spring");
        String packageDir = String.join(File.separator, targetDir, "io", "openapiprocessor", "openapi");
        String apiPackage = String.join(File.separator, packageDir, "api");
        String modelPackage = String.join(File.separator, packageDir, "model");
        String supportPackage = String.join(File.separator, packageDir, "support");

        assertThat(result)
                .isSuccessful()
                .project()
                .has(targetDir)
                .has(packageDir)
                .has(apiPackage)
                .has(modelPackage)
                .has(supportPackage);

        assertThat(result)
            .out()
            .info()
            .contains("   apiPath - " + joinDirs("${project.basedir}", "src", "api", "openapi.yaml"))
            .contains(" targetDir - " + joinDirs("${project.basedir}", "target", "generated-sources", "spring"));
    }

    private String joinDirs (CharSequence... directories) {
        return String.join(File.separator, directories);
    }
}
