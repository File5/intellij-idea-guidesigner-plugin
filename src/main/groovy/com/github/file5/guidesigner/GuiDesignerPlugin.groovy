package com.github.file5.guidesigner

import org.gradle.api.Plugin
import org.gradle.api.Project

class GuiDesignerPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        GuiDesignerModule.load(project)
    }
}
