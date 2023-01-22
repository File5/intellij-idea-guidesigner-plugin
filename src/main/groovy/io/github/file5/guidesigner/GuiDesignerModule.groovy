package io.github.file5.guidesigner

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class GuiDesignerModule {
    static void load(Project project) {
        // https://discuss.gradle.org/t/adding-stuff-to-tasks-when-plugin-order-is-unknown/9236/2
        project.plugins.withType(JavaPlugin) {
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
                it.add project.dependencies.create(project.sourceSets.main.runtimeClasspath)
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
                                srcdir: project.sourceSets.main.allSource.sourceDirectories.filter {
                                    it.directory
                                }.asPath,
                                destdir: it.path,
                                classpath: formsClasspath.asPath
                        )
                    }
                }
            }
            def compileJava = project.tasks.findByName "compileJava"
            if (compileJava != null) {
                instrumentForms.dependsOn compileJava
            }
            def compileKotlin = project.tasks.findByName "compileKotlin"
            if (compileKotlin != null) {
                instrumentForms.dependsOn compileKotlin
            }
            def classes = project.tasks.findByName "classes"
            if (classes != null) {
                classes.dependsOn instrumentForms
            }
        }
    }
}
