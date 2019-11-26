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
    public static final String SITEWHERE_DIR = "/home/gradle/sitewhere";

    /** SiteWhere configuration information */
    ISiteWhereConfiguration siteWhereConfiguration;

    @Override
    public void create() {
	destFile.set(project.layout.buildDirectory.file('docker/Dockerfile'))

	// Execute native build in GraalVM container.
	from "${siteWhereConfiguration.nativeImage.graalImage.get()} as graalvm"
	copyFile(new CopyFile("./m2", "/root/.m2/repository"))
	copyFile(new CopyFile("./sitewhere", SITEWHERE_DIR))
	workingDir(SITEWHERE_DIR);
	runCommand("/opt/gradle/bin/gradle build");
	runCommand("/opt/graalvm-ce-19.2.1/bin/gu install native-image");
	workingDir(SITEWHERE_DIR + "/${project.name}");
	runCommand("/opt/gradle/bin/gradle buildNative");
	
	// Copy SSL libraries.
	from "quay.io/quarkus/ubi-quarkus-native-image:19.2.1 as ssllibs"
	runCommand "mkdir -p /tmp/ssl-libs/lib && cp /opt/graalvm/jre/lib/security/cacerts /tmp/ssl-libs && cp /opt/graalvm/jre/lib/amd64/libsunec.so /tmp/ssl-libs/lib/"
	
	// Build small container with executable.
	def runner = "${project.name}-${project.version}-runner"
	from "frolvlad/alpine-glibc"
	copyFile(new CopyFile(SITEWHERE_DIR + "/${project.name}/build/${runner}", ".").withStage("graalvm"));
	copyFile(new CopyFile("/tmp/ssl-libs", "/ssllib").withStage("ssllibs"));
	runCommand("chmod 775 /ssllib");
	defaultCommand("./${runner}", "-Dquarkus.http.host=0.0.0.0", "-Djava.library.path=/ssllib/lib", "-Djavax.net.ssl.trustStore=/ssllib/cacerts")

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
