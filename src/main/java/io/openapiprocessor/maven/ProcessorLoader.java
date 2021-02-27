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

import java.util.*;

/**
 * Find the processors using the ServiceLoader.
 *
 * @author Martin Hauner
 */
class ProcessorLoader {


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
