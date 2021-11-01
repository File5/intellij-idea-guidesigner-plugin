package io.github.file5.guidesigner;

import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.gradle.internal.impldep.org.junit.Assert.assertTrue;

public class GuiDesignerPluginTest {

    @Test
    public void testAddMavenRepositories() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("io.github.file5.guidesigner");

        boolean jetbrainsRepo = false;
        boolean thirdPartyRepo = false;
        for (ArtifactRepository repository : project.getRepositories()) {
            if (repository.getName().equals("instrumentFormsRepository-jetbrains")) {
                jetbrainsRepo = true;
            } else if (repository.getName().equals("instrumentFormsRepository-third-party")) {
                thirdPartyRepo = true;
            }
        }
        assertTrue("jetbrains repo is not added", jetbrainsRepo);
        assertTrue("third-party repo is not added", thirdPartyRepo);
    }

    @Test
    public void testCreatesFormsClasspathConfiguration() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("io.github.file5.guidesigner");

        // If not exist - throws UnknownConfigurationException
        project.getConfigurations().getByName("formsClasspath");
    }

    @Test
    public void testCreatesInstrumentFormsTask() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("io.github.file5.guidesigner");

        // If not exist - throws UnknownTaskException
        project.getTasks().getByName("instrumentForms");
    }

    @Test
    public void testUnknownOrderOfPluginsApplied() {
        // https://github.com/File5/intellij-idea-guidesigner-plugin/issues/1
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("io.github.file5.guidesigner");
        project.getPluginManager().apply("java");

        // If not exist - throws UnknownConfigurationException
        project.getConfigurations().getByName("formsClasspath");
    }
}
