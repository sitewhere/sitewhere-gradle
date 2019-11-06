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

import org.gradle.api.tasks.TaskAction

import com.bmuschko.gradle.docker.tasks.image.Dockerfile

/**
 * Create Dockerfile to build a native image.
 */
class NativeImageDockerfile extends Dockerfile {

    /** Working directory for docker artifacts */
    public static final String WORKING_DIR = "/home/gradle/sitewhere";

    public NativeImageDockerfile() {
	super()
	destFile.set(project.layout.buildDirectory.file('docker/Dockerfile'))

	// Execute Gradle build stage.
	from "${project.sitewhere.gradleImage.get()} as builder"
	copyFile(new CopyFile(".", WORKING_DIR).withChown("gradle:gradle"))
	workingDir(WORKING_DIR);
	runCommand("./gradlew build");

	// Execute native build in GraalVM container.
	from "oracle/graalvm-ce:19.2.1 as graalvm"
	copyFile(new CopyFile(WORKING_DIR, WORKING_DIR).withStage("builder"));
	workingDir(WORKING_DIR);
	runCommand("/opt/graalvm-ce-19.2.1/bin/gu install native-image");
	runCommand("./gradlew buildNative");

	// Execute native build in GraalVM container.
	def runner = "sitewhere-${project.version}-runner"
	from "frolvlad/alpine-glibc"
	copyFile(new CopyFile("/home/gradle/sitewhere-graalvm/build/${runner}", ".").withStage("graalvm"));
	entryPoint("./${runner}")
    }
}
