package com.sos.administration;

import org.apache.maven.plugin.MojoExecutionException;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Version {

    private final DateTime NOW = new DateTime();
    private final String TIME_KEY = getTimeKey(NOW);

    private final String groupId;
    private final String artifactId;
    private final Integer major;
    private final Integer minor;
    private final Boolean newMajor;
    private final Boolean newMinor;
    private final String targetTypeId;
    private final TargetType targetType;

    public Version(String groupId, String artifactId, String major, String minor, String build, boolean newMajor, boolean newMinor, String targetTypeId) throws MojoExecutionException {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.major = Integer.parseInt(major);
        this.minor = Integer.parseInt(minor);
        this.newMajor = newMajor;
        this.newMinor = newMinor;
        this.targetTypeId = targetTypeId;
        this.targetType = TargetType.asType(targetTypeId);
        if(targetType == null)
            throw new MojoExecutionException( "The targetType " + targetType + " is not valid. Valid values are " + TargetType.asString() + ".");
    }

    public String getMajorMinor() {
        return major + "." + minor;
    }

    public String getReleaseVersion() {
        return getMajorMinor() + getDelimiter() + getReleaseQualifier();
    }

    private String getReleaseQualifier() {
        return (targetType == TargetType.release) ? TIME_KEY : targetTypeId;
    }

    private String getDelimiter() {
        return (targetType == TargetType.release) ? "." : "-";
    }

    public String getReleaseVersionProperty() {
        return "project.rel." + groupId + "\\:" + artifactId + "=" + getReleaseVersion();
    }

    public String getDevelopmentVersion() {
        Integer nextMajor = major;
        Integer nextMinor = minor;
        if (newMajor) {
            nextMajor++;
            nextMinor = 0;
        } else {
            if (newMinor) {
                nextMinor++;
            }
        }
        return nextMajor + "." + nextMinor + "-SNAPSHOT";
    }

    public String getDevelopmentVersionProperty() {
        return "project.dev." + groupId + "\\:" + artifactId + "=" + getDevelopmentVersion();
    }

    private String getTimeKey(DateTime forDate) {
        DateFormat format = new SimpleDateFormat("yyDDD");
        return format.format(forDate.toDate()).substring(1);
    }

}
