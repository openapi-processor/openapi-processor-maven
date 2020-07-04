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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Find the processors using the ServiceLoader.
 *
 * @author Martin Hauner
 */
class ProcessorLoader {

    static List<OpenApiProcessor> load(ClassLoader classLoader) {
        return asList (ServiceLoader.load (OpenApiProcessor.class, classLoader));
    }

    static List<OpenApiProcessor> asList(Iterable<OpenApiProcessor> processors) {
        return StreamSupport.stream (processors.spliterator(), false)
            .collect(Collectors.toList());
    }

}
