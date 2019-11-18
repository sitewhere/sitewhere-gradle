/*
 * Copyright 2019 SiteWhere LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sitewhere.configuration

import javax.inject.Inject

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

/**
 * Configuration for native image generation.
 */
class StandardConfiguration {
    
    /** Extension name */
    public static final String EXTENSION_NAME = "standardImage";

    /** Default JVM image used for generation */
    private static final String DEFAULT_JVM_IMAGE = "oracle/graalvm-ce:19.2.1"
    
    /** Default Docker repository for image */
    private static final String DEFAULT_DOCKER_REPOSITORY = "docker.io"

    /**
     * JVM Docker image used for generation.
     */
    final Property<String> jvmImage
    
    /**
     * Docker repository for generated image.
     */
    final Property<String> dockerRepository

    @Inject
    StandardConfiguration(ObjectFactory objectFactory) {
	jvmImage = objectFactory.property(String).convention(DEFAULT_JVM_IMAGE)
	dockerRepository = objectFactory.property(String).convention(DEFAULT_DOCKER_REPOSITORY)
    }
}
