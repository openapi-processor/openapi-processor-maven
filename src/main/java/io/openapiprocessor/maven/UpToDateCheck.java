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

import org.apache.maven.shared.utils.io.DirectoryScanner;

import java.io.File;

/**
 * check if any input y(a)ml file is newer than any output file.
 *
 * @author Martin Hauner
 */
public class UpToDateCheck {

    /**
     * checks if any *.yaml or *.yml input file is newer than any output file.
     *
     * It returns true if the last modified date of any input file is younger than all modified
     * dates of the outputs or if the outputs directory is empty.
     *
     * @param input root directory of the input files
     * @param output target directory of the output files
     * @return true in case the input is younger than the output, otherwise false
     */
    public boolean isUpToDate (File input, File output) {
        long lastModifiedInput = getLastModified (input, scanInput (input));
        long lastModifiedOutput = getLastModified (output, scanOutput (output));
        return lastModifiedInput <= lastModifiedOutput;
    }

    private String[] scanInput (File source) {
        DirectoryScanner sourceScanner = new DirectoryScanner ();
        sourceScanner.setBasedir (source);
        sourceScanner.setIncludes ("**/*.yaml", "**/*.yml");
        sourceScanner.scan ();
        return sourceScanner.getIncludedFiles ();
    }

    private String[] scanOutput (File output) {
        if (!output.exists ()) {
            return new String[0];
        }

        DirectoryScanner targetScanner = new DirectoryScanner ();
        targetScanner.setBasedir (output);
        targetScanner.setIncludes ("**/*", "**/*");
        targetScanner.scan ();
        return targetScanner.getIncludedFiles ();
    }

    private long getLastModified (File root, String[] files) {
        long lastModified = 0;

        for (String file : files) {
            File current = new File (root, file);

            if (current.exists () && current.lastModified () > lastModified) {
                lastModified = current.lastModified ();
            }
        }

        return lastModified;
    }

}
