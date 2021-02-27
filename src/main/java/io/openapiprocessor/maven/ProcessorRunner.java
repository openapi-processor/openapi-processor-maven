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
            Optional<io.openapiprocessor.api.v1.OpenApiProcessor> processorV1 = ProcessorLoader
                .findProcessorV1 (processorName, this.getClass ().getClassLoader ());

            if(processorV1.isPresent ()) {
                processorV1.get ().run (processorProps);
                return;
            }

            Optional<io.openapiprocessor.api.OpenApiProcessor> processor = ProcessorLoader
                .findProcessor (processorName, this.getClass ().getClassLoader ());

            if(processor.isPresent ()) {
                processor.get ().run (processorProps);
                return;
            }

            Optional<com.github.hauner.openapi.api.OpenApiProcessor> processorOld = ProcessorLoader
                .findProcessorOld (processorName, this.getClass ().getClassLoader ());

            processorOld.ifPresent (openApiProcessor -> openApiProcessor.run (processorProps));

        } catch (Exception e) {
            throw e;
        }
    }

}
