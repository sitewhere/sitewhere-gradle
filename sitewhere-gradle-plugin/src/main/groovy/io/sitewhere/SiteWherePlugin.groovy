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
package io.sitewhere;
import org.apache.tools.ant.DirectoryScanner
import org.gradle.api.Action
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.Copy

import io.sitewhere.configuration.DebugConfiguration
import io.sitewhere.configuration.NativeConfiguration
import io.sitewhere.configuration.SiteWhereConfiguration
import io.sitewhere.configuration.StandardConfiguration

/**
 * SiteWhere Gradle plugin.
 */
public class SiteWherePlugin implements Plugin<Project> {

    /** Constant for standard image Dockerfile task */
    public static final String TASK_STANDARD_IMAGE_DOCKER_FILE = "standardImageDockerfile";

    /** Constant for native image Dockerfile task */
    public static final String TASK_NATIVE_IMAGE_DOCKER_FILE = "nativeImageDockerfile";

    /** Constant for standard image generation task */
    public static final String TASK_DOCKER_STANDARD_IMAGE = "dockerImage";

    /** Constant for native image generation task */
    public static final String TASK_DOCKER_NATIVE_IMAGE = "dockerNativeImage";

    /*
     * @see org.gradle.api.Plugin#apply(java.lang.Object)
     */
    @Override
    void apply(Project project) {
	// Create configuration extensions.
	SiteWhereConfiguration sitewhere = project.extensions.create(SiteWhereConfiguration.EXTENSION_NAME, SiteWhereConfiguration, project.objects)
	((ExtensionAware) sitewhere).extensions.create(DebugConfiguration.EXTENSION_NAME, DebugConfiguration, project.objects)
	((ExtensionAware) sitewhere).extensions.create(StandardConfiguration.EXTENSION_NAME, StandardConfiguration, project.objects)
	((ExtensionAware) sitewhere).extensions.create(NativeConfiguration.EXTENSION_NAME, NativeConfiguration, project.objects)

	// Copy jar file and libraries.
	project.getTasks().create("copyJarAndLibs", Copy) {
	    from(project.layout.buildDirectory)
	    into(project.layout.buildDirectory.dir('docker'))
	    include "${project.name}-${project.version}-runner.jar"
	    include 'lib/**'
	}.dependsOn("build")

	// Copy microservice core.
	project.getTasks().create("copyMsCoreToDocker", Copy) {
	    from(new File(project.parent.projectDir, '../sitewhere-microservice-core'))
	    into(project.layout.buildDirectory.file('docker/sitewhere-microservice-core'))
	}

	// Copy core code for building in GraalVM.
	project.getTasks().create("copyCodeToDocker", Copy) {
	    // Hack to allow copying of excluded resources.
	    doFirst {
		DirectoryScanner.defaultExcludes.each { DirectoryScanner.removeDefaultExclude it }
		DirectoryScanner.addDefaultExclude 'placeholder'
	    }

	    from(project.parent.projectDir)
	    into(project.layout.buildDirectory.file('docker'))
	    include 'gradlew'
	    include 'gradle/**'
	    include 'build.gradle'
	    include 'gradle.properties'
	    include 'settings.gradle'
	    include '.git/**'
	    include 'HEADER'
	    include "${project.name}/src/**"
	    include "${project.name}/build.gradle"
	    include "sitewhere-communication/**"
	    include "sitewhere-configuration/**"
	    include "sitewhere-mongodb/**"

	    doLast {
		DirectoryScanner.resetDefaultExcludes()
	    }
	}.dependsOn("copyMsCoreToDocker")

	project.getTasks().create(TASK_STANDARD_IMAGE_DOCKER_FILE, StandardImageDockerfile.class).dependsOn("copyJarAndLibs")
	project.getTasks().create(TASK_DOCKER_STANDARD_IMAGE, DockerStandardImage.class).dependsOn(TASK_STANDARD_IMAGE_DOCKER_FILE)

	project.getTasks().create(TASK_NATIVE_IMAGE_DOCKER_FILE, NativeImageDockerfile.class).dependsOn("copyCodeToDocker")
	project.getTasks().create(TASK_DOCKER_NATIVE_IMAGE, DockerNativeImage.class).dependsOn(TASK_NATIVE_IMAGE_DOCKER_FILE)
	configureSiteWhereAwareTasks(project, sitewhere)
    }

    /**
     * Configure SiteWhere tasks with extension info.
     * @param project
     * @param sitewhere
     */
    private void configureSiteWhereAwareTasks(Project project, SiteWhereConfiguration sitewhere) {
	project.tasks.withType(SiteWhereAware, new Action<SiteWhereAware>() {
		    @Override
		    void execute(SiteWhereAware siteWhereAware) {
			siteWhereAware.setSiteWhereConfiguration(sitewhere)
		    }
		})
    }
}
