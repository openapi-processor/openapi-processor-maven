/*
 * Copyright 2019 https://github.com/openapi-processor/openapi-processor-maven
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.maven;

import java.util.Map;
import java.util.Optional;

/**
 * Runs the processor with the classes class loader which will include the dependencies from the
 * openapiProcessor configuration.
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
