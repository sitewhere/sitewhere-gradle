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
 * Create Dockerfile to build a native image.
 */
class NativeImageDockerfile extends Dockerfile implements SiteWhereAware {

    /** Working directory for docker artifacts */
    public static final String WORKING_DIR = "/home/gradle/sitewhere";

    /** Working directory for microservice core artifacts */
    public static final String MICOSERVICE_DIR = WORKING_DIR + "/sitewhere-microservice-core";

    /** SiteWhere configuration information */
    ISiteWhereConfiguration siteWhereConfiguration;

    @Override
    public void create() {
	destFile.set(project.layout.buildDirectory.file('docker/Dockerfile'))

	// Execute native build in GraalVM container.
	from "${siteWhereConfiguration.nativeImage.graalImage.get()} as graalvm"
	copyFile(new CopyFile(".", WORKING_DIR))
	// workingDir(MICOSERVICE_DIR);
	// runCommand("/opt/gradle/bin/gradle publishToMavenLocal");
	workingDir(WORKING_DIR);
	runCommand("/opt/gradle/bin/gradle build");
	runCommand("/opt/graalvm-ce-19.2.1/bin/gu install native-image");
	workingDir(WORKING_DIR + "/${project.name}");
	runCommand("/opt/gradle/bin/gradle buildNative");

	// Execute native build in GraalVM container.
	def runner = "${project.name}-${project.version}-runner"
	from "frolvlad/alpine-glibc"
	copyFile(new CopyFile(WORKING_DIR + "/${project.name}/build/${runner}", ".").withStage("graalvm"));
	entryPoint("./${runner}")

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
