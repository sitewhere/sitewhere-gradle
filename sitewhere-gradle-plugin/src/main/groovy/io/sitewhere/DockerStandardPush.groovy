/*
 * Copyright 2020 SiteWhere LLC.
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

import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

import io.sitewhere.configuration.ISiteWhereConfiguration

/**
 * Gradle task which pushes a standard JVM image.
 */
class DockerStandardPush extends DockerPushImage implements SiteWhereAware {

    /** SiteWhere configuration information */
    ISiteWhereConfiguration siteWhereConfiguration;

    /** Set SiteWhere configuration information **/
    void setSiteWhereConfiguration(ISiteWhereConfiguration sitewhere) {
	this.siteWhereConfiguration = sitewhere;
	String pushImageName = "${siteWhereConfiguration.standardImage.dockerRepository.get()}/sitewhere/${project.name}"
	String pushTag = "${project.version}"
	imageName.set(pushImageName)
	tag.set(pushTag)
	logger.info(String.format("Pushing microservice standard image '%s' with tag '%s'...", pushImageName, pushTag))
    }
}
