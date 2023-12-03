/*
 * Copyright 2019 https://github.com/openapi-processor/openapi-processor-maven
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.maven;

import java.util.*;

/**
 * find the processors using the ServiceLoader.
 */
class ProcessorLoader {

    public static Optional<io.openapiprocessor.api.v2.OpenApiProcessor> findProcessorV2(
        String processorName, ClassLoader classLoader) {

        List<io.openapiprocessor.api.v2.OpenApiProcessor> processors = new ArrayList<> ();

        ServiceLoader.load (io.openapiprocessor.api.v2.OpenApiProcessor.class, classLoader)
            .forEach (processors::add);

        return processors
            .stream()
            .filter (p -> p.getName ().equals (processorName))
            .findFirst ();
    }

    public static Optional<io.openapiprocessor.api.v1.OpenApiProcessor> findProcessorV1(
        String processorName, ClassLoader classLoader) {

        List<io.openapiprocessor.api.v1.OpenApiProcessor> processors = new ArrayList<> ();

        ServiceLoader.load (io.openapiprocessor.api.v1.OpenApiProcessor.class, classLoader)
            .forEach (processors::add);

        return processors
            .stream()
            .filter (p -> p.getName ().equals (processorName))
            .findFirst ();
    }

    public static Optional<io.openapiprocessor.api.OpenApiProcessor> findProcessor(
        String processorName, ClassLoader classLoader) {

        List<io.openapiprocessor.api.OpenApiProcessor> processors = new ArrayList<> ();

        ServiceLoader.load (io.openapiprocessor.api.OpenApiProcessor.class, classLoader)
            .forEach (processors::add);

        return processors
            .stream()
            .filter (p -> p.getName ().equals (processorName))
            .findFirst ();
    }

    public static Optional<com.github.hauner.openapi.api.OpenApiProcessor> findProcessorOld(
        String processorName, ClassLoader classLoader) {

        List<com.github.hauner.openapi.api.OpenApiProcessor> processors = new ArrayList<> ();

        ServiceLoader.load (com.github.hauner.openapi.api.OpenApiProcessor.class, classLoader)
            .forEach (processors::add);

        return processors
            .stream()
            .filter (p -> p.getName ().equals (processorName))
            .findFirst ();
    }

}
