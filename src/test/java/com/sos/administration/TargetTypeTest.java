package com.sos.administration;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TargetTypeTest {

    @Test
    public void releaseTest() throws MojoExecutionException {
        assertEquals(TargetType.release,TargetType.asType("RELEASE"));
    }

    @Test
    public void releaseCandidateTest() throws MojoExecutionException {
        assertEquals(TargetType.release_candidate,TargetType.asType("RC1"));
    }

    @Test
    public void milestoneTest() throws MojoExecutionException {
        assertEquals(TargetType.milestone,TargetType.asType("M1"));
    }

    @Test(expected = MojoExecutionException.class)
    public void invalidReleaseCandidateTest() throws MojoExecutionException {
        assertEquals(TargetType.release_candidate,TargetType.asType("RCabc"));
    }

    @Test(expected = MojoExecutionException.class )
    public void invalidMilestoneTest() throws MojoExecutionException {
        assertEquals(TargetType.release_candidate,TargetType.asType("M"));
    }

}
