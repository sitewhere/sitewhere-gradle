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

import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

import io.sitewhere.configuration.ISiteWhereConfiguration

/**
 * Gradle task which creates a standard JVM image.
 */
class DockerStandardImage extends DockerBuildImage implements SiteWhereAware {

    /** SiteWhere configuration information */
    ISiteWhereConfiguration siteWhereConfiguration;

    /** Set SiteWhere configuration information **/
    void setSiteWhereConfiguration(ISiteWhereConfiguration sitewhere) {
	this.siteWhereConfiguration = sitewhere;
	dockerFile.set(project.layout.buildDirectory.file('docker/Dockerfile'))
	String tag = "${siteWhereConfiguration.standardImage.dockerRepository.get()}/sitewhere/${project.name}:${project.version}"
	tags.set([tag])
	logger.info(String.format("Building microservice native image with tag '%s'...", tag))
    }
}
