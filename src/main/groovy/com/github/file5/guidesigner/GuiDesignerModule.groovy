package com.github.file5.guidesigner

import org.gradle.api.Project

class GuiDesignerModule {
    static void load(Project project) {
        def antClasspath = project.configurations.create('antClasspath')
        antClasspath.defaultDependencies {
            it.add project.dependencies.create("com.jetbrains.intellij.java:java-compiler-ant-tasks")
            it.add project.dependencies.create("com.jetbrains.intellij.java:java-gui-forms-rt")
        }

        def instrumentForms = project.task('instrumentForms') {
            doLast {
                ant.echo "Patching GUI Designer form binding .class files"
                ant.taskdef(
                    name: "javac2",
                    classname: "com.intellij.ant.InstrumentIdeaExtensions",
                    classpath: project.configurations.antClasspath.asPath
                )
                ant.javac2(
                    includeantruntime: false,
                    srcdir: project.sourceSets.main.java.sourceDirectories.asPath,
                    destdir: project.sourceSets.main.output.classesDirs.filter {
                        it.absolutePath.contains "java"
                    }.asPath,
                )
            }
        }
        project.tasks.classes.dependsOn instrumentForms
    }
}
