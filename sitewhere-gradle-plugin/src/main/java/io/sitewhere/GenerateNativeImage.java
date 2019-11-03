/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package io.sitewhere;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

/**
 * Gradle task which creates a native image.
 */
public class GenerateNativeImage extends DefaultTask {

    /**
     * Action which generates a native image.
     * 
     * @throws Exception
     */
    @TaskAction
    public void generateNativeImage() throws Exception {
    }
}
