# IntelliJ IDEA GUI Designer Gradle plugin

## Note

This open-source project is not related to [JetBrains](https://www.jetbrains.com) company and
does not claim any rights on JetBrains or IntelliJ IDEA brands. These are only mentioned to
inform people about compatibility and usage of this software. The information about the usage of
the brands "JetBrains" and "IntelliJ IDEA" can be found [here](https://www.jetbrains.com/company/brand/).

## Overview

IntelliJ IDEA has a GUI Designer to create Swing GUI Forms visually. However, the compilation of the
forms happens only when IntelliJ IDEA itself is responsible for compilation
(Build, Execution, Deployment > Build Tools > Gradle > Gradle projects > Build and run using: IntelliJ IDEA).
There are some outdated Maven plugins to compile forms, but there are no such for Gradle.

This Gradle plugin enables to instrument the compiled classes bound to the forms with the UI creation code.

## Usage

Apply the Gradle plugin in the module `build.gradle` (or project `build.gradle` if there are no modules)
after other plugins.

```groovy
plugins {
    // ...
    id 'io.github.file5.guidesigner' version '1.0.2'
}
```

Add IntelliJ IDEA form components dependency in the module `build.gradle`.

```groovy
dependencies {
    // ...
    implementation "com.jetbrains.intellij.java:java-gui-forms-rt:+"
    //noinspection GradlePackageUpdate
    implementation "com.jgoodies:forms:1.1-preview"
}
```
