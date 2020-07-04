/*
 * Copyright 2019-2020 the original authors
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

import com.github.hauner.openapi.api.OpenApiProcessor;

import java.util.Map;
import java.util.Optional;

/**
 * Runs the processor with the classes class loader which will include the dependencies from the
 * openapiProcessor configuration.
 *
 * @author Martin Hauner
 */
class ProcessorRunner {

    private final String processorName;
    private final Map<String, ?> processorProps;

    ProcessorRunner (String processorName, Map<String, ?> processorProps) {
        this.processorName = processorName;
        this.processorProps = processorProps;
    }

    void run () {
        try {
            getProcessor ().ifPresent (p -> {
                p.run (processorProps);
            });
        } catch (Exception e) {
            throw e;
        }
    }

    private Optional<OpenApiProcessor> getProcessor () {
        return ProcessorLoader.load (this.getClass ().getClassLoader ())
            .stream ()
            .filter (p -> p.getName ().equals (processorName))
            .findFirst ();
    }

}
