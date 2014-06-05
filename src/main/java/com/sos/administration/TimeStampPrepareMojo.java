package com.sos.administration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mojo( name = "prepare", aggregator = true)
public class TimeStampPrepareMojo extends AbstractMojo
{

    private final static String VERSION_STRING_PATTERN = "^([^\\.-]+)\\.([^\\.-]+)-SNAPSHOT";

    // @Parameter( defaultValue = "${project}" )
    // private org.apache.maven.project.MavenProject project;

    @Parameter( property = "targetType", defaultValue = "RELEASE", required = false, readonly = true )
    private String targetTypeId;

    @Parameter( property = "newMajor", defaultValue = "false", required = false, readonly = true )
    private Boolean newMajor;

    @Parameter( property = "newMinor", defaultValue = "false", required = false, readonly = true )
    private Boolean newMinor;

    @Parameter( defaultValue = "${basedir}", readonly = true )
    private File outputDirectory;

    @Parameter( defaultValue = "${project.groupId}", readonly = true )
    private String groupId;

    @Parameter( defaultValue = "${project.artifactId}", readonly = true )
    private String artifactId;

    @Parameter( defaultValue = "${project.version}", readonly = true )
    private String version;


    public void execute() throws MojoExecutionException, MojoFailureException {

        Pattern p = Pattern.compile(VERSION_STRING_PATTERN);
        Matcher m = p.matcher(version);
        if (!m.find()) {
            throw new MojoExecutionException( "The version number should have the format <major>.<minor>-SNAPSHOT, but is " + version);
        }
        if (newMajor) {
            getLog().info("A new major version will be created for development.");
        }
        if (newMinor) {
            getLog().info("A new minor version will be created for development.");
        }

        String major = m.group(1);
        String minor = m.group(2);
        String build = (m.groupCount()>3) ? m.group(3).toUpperCase() : "";
        Version v = new Version(groupId, artifactId, major, minor, build, newMajor, newMinor, targetTypeId);

        getLog().info("The release version is " + v.getReleaseVersion());
        getLog().info("The new development version is " + v.getDevelopmentVersion());
        writePropertiesFile(v);

        // project.getProperties().setProperty("releaseVersion", v.getReleaseVersion() );
        // project.getProperties().setProperty("developmentVersion", v.getDevelopmentVersion() );
        // super.execute();

    }

    private void writePropertiesFile(Version version) throws MojoExecutionException {
        createOutputDirectory(outputDirectory);
        File touch = new File( outputDirectory, "release.properties" );

        FileWriter w = null;
        try
        {
            w = new FileWriter( touch );
            w.write( version.getReleaseVersionProperty() + "\n" );
            w.write( version.getDevelopmentVersionProperty() + "\n");
        }
        catch ( IOException e ) {
            throw new MojoExecutionException( "Error creating file " + touch, e );
        } finally {
            if ( w != null ) {
                try {
                    w.close();
                } catch ( IOException e ) {
                    getLog().warn("The FileWriter object for file " + touch.getAbsolutePath() + " could not be closed.");
                }
            }
        }
    }

    private static void createOutputDirectory(File outputDirectory) {
        if ( !outputDirectory.exists() ) {
            outputDirectory.mkdirs();
        }
    }

}
