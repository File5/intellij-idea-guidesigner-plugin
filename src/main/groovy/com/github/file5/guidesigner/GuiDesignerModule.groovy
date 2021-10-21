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

        def formsClasspath = project.configurations.create('formsClasspath')
        formsClasspath.extendsFrom project.configurations.implementation
        formsClasspath.defaultDependencies {
            it.add project.dependencies.create("com.jetbrains.intellij.java:java-compiler-ant-tasks:+")
            it.add project.dependencies.create("com.jetbrains.intellij.java:java-gui-forms-rt:+")
        }

        def instrumentForms = project.task('instrumentForms') {
            doLast {
                ant.echo "Patching GUI Designer form binding .class files"
                ant.taskdef(
                    name: "javac2",
                    classname: "com.intellij.ant.InstrumentIdeaExtensions",
                    classpath: formsClasspath.asPath
                )
                project.sourceSets.main.output.classesDirs.filter {
                    it.directory
                }.forEach {
                    ant.javac2(
                        includeantruntime: false,
                        srcdir: project.sourceSets.main.allSource.sourceDirectories.asPath,
                        destdir: it.path,
                    )
                }
            }
        }
        if (project.tasks.findByName "compileJava") {
            instrumentForms.dependsOn project.tasks.compileJava
        }
        if (project.tasks.findByName "compileKotlin") {
            instrumentForms.dependsOn project.tasks.compileKotlin
        }
        if (project.tasks.findByName "classes") {
            project.tasks.classes.dependsOn instrumentForms
        }
    }
}
