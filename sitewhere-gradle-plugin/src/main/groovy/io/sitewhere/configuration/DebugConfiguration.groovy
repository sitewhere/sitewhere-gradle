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
 * Holds debug settings.
 */
class DebugConfiguration {

    /** Extension name */
    public static final String EXTENSION_NAME = "debug";

    /** Default for debug enabled flag */
    private static final Boolean DEFAULT_DEBUG_ENABLED = new Boolean(false);

    /**
     * Enable/disable debugging.
     */
    final Property<Boolean> enabled;

    @Inject
    public DebugConfiguration(ObjectFactory objectFactory) {
	enabled = objectFactory.property(Boolean).convention(DEFAULT_DEBUG_ENABLED)
    }
}
