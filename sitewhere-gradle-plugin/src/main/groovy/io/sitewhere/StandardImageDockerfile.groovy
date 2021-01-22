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

import com.bmuschko.gradle.docker.tasks.image.Dockerfile

import io.sitewhere.configuration.ISiteWhereConfiguration

/**
 * Create Dockerfile to build a standard JVM-based image.
 */
class StandardImageDockerfile extends Dockerfile implements SiteWhereAware {

    /** SiteWhere configuration information */
    ISiteWhereConfiguration siteWhereConfiguration;

    @Override
    public void create() {
	destFile.set(project.layout.buildDirectory.file('docker/Dockerfile'))
	def runnerJar = "${project.name}-${project.version}-runner.jar"

	// Execute native build in GraalVM container.
	from "${siteWhereConfiguration.standardImage.dockerRepository.get()}/${siteWhereConfiguration.standardImage.jvmImage.get()} as jvm"
	copyFile(runnerJar, "/")
	copyFile("lib", "/lib")
	defaultCommand('java',
		'-Xmx512M',
		'-Xss384K',
		'-jar',
		"/${runnerJar}");

	destFile.get().asFile.withWriter { out ->
	    instructions.get().forEach() { Instruction instruction ->
		String instructionText = instruction.getText()

		if (instructionText) {
		    out.println instructionText
		}
	    }
	}
    }
}
