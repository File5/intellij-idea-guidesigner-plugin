package com.github.file5.guidesigner

import org.gradle.api.Project

class GuiDesignerModule {
    static void load(Project project) {
        project.repositories.maven {
            name "instrumentFormsRepository-jetbrains"
            url "https://www.jetbrains.com/intellij-repository/releases"
        }
        project.repositories.maven {
            name "instrumentFormsRepository-third-party"
            url "https://cache-redirector.jetbrains.com/intellij-dependencies"
        }

        def antClasspath = project.configurations.create('antClasspath')
        antClasspath.extendsFrom project.configurations.implementation
        antClasspath.defaultDependencies {
            it.add project.dependencies.create("com.jetbrains.intellij.java:java-compiler-ant-tasks:+")
            it.add project.dependencies.create("com.jetbrains.intellij.java:java-gui-forms-rt:+")
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
        instrumentForms.dependsOn project.tasks.compileJava
        project.tasks.classes.dependsOn instrumentForms
    }
}
