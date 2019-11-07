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
class NativeConfiguration {
    
    /** Extension name */
    public static final String EXTENSION_NAME = "nativeImage";

    /** Default Gradle image used for running Gradle build */
    private static final String DEFAULT_GRADLE_IMAGE = "gradle:jdk8"

    /** Default GraalVM image used for generation */
    private static final String DEFAULT_GRAAL_IMAGE = "oracle/graalvm-ce:19.2.1"

    /**
     * Gradle Docker image used for running Gradle build.
     */
    final Property<String> gradleImage

    /**
     * GraalVM Docker image used for generation.
     */
    final Property<String> graalImage

    @Inject
    NativeConfiguration(ObjectFactory objectFactory) {
	gradleImage = objectFactory.property(String).convention(DEFAULT_GRADLE_IMAGE)
	graalImage = objectFactory.property(String).convention(DEFAULT_GRAAL_IMAGE)
    }
}
