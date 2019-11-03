/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package io.sitewhere;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SiteWherePlugin implements Plugin<Project> {

    /** Extension name */
    private static final String EXTENSION_NAME = "sitewhere";

    /** Gradle project */
    private Project project;

    /*
     * @see org.gradle.api.Plugin#apply(java.lang.Object)
     */
    @Override
    public void apply(Project project) {
	this.project = project;
	configure();
    }

    /**
     * Configure extension and tasks.
     */
    protected void configure() {
	getProject().getExtensions().create(EXTENSION_NAME, SiteWhereExtension.class);
	getProject().getTasks().create("generateNativeImage", GenerateNativeImage.class);
    }

    protected Project getProject() {
	return project;
    }
}
