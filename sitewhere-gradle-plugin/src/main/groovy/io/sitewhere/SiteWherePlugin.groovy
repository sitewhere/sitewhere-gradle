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
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * SiteWhere Gradle plugin.
 */
public class SiteWherePlugin implements Plugin<Project> {

    /** Extension name */
    public static final String EXTENSION_NAME = "sitewhere";

    /** Constant for native image Dockerfile task */
    public static final String TASK_NATIVE_IMAGE_DOCKER_FILE = "nativeImageDockerfile";

    /** Constant for native image generation task */
    public static final String TASK_GENERATE_NATIVE_IMAGE = "generateNativeImage";

    /*
     * @see org.gradle.api.Plugin#apply(java.lang.Object)
     */
    @Override
    void apply(Project project) {
	project.extensions.create(EXTENSION_NAME, SiteWhereExtension, project.objects)
	project.getTasks().create(TASK_NATIVE_IMAGE_DOCKER_FILE, NativeImageDockerfile.class).dependsOn("build");
	project.getTasks().create(TASK_GENERATE_NATIVE_IMAGE, GenerateNativeImage.class).dependsOn(TASK_NATIVE_IMAGE_DOCKER_FILE);
    }

    public SiteWhereExtension getSiteWhereExtension() {
	return siteWhereExtension;
    }
}
