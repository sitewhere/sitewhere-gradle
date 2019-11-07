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
package io.sitewhere

import org.gradle.api.Task
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional

import groovy.transform.CompileStatic
import io.sitewhere.configuration.ISiteWhereConfiguration

@CompileStatic
interface SiteWhereAware extends Task {

    /** Set SiteWhere configuration information **/
    void setSiteWhereConfiguration(ISiteWhereConfiguration sitewhere)

    /**
     * Get SiteWhere configuration information in a task.
     * @return
     */
    @Nested
    @Optional
    ISiteWhereConfiguration getSiteWhereConfiguration()
}
