package com.sos.administration;

import org.apache.maven.plugin.MojoExecutionException;

public enum TargetType {
    release("RELEASE"),
    release_candidate("RC"),
    milestone("M");

    private String typeId;

    private TargetType(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public static TargetType asType(String typeId) throws MojoExecutionException {
        String ts = typeId.toUpperCase();
        for(TargetType t : TargetType.values()) {
            if(ts.startsWith(t.getTypeId())) {
                if(t != TargetType.release) {
                    String numString = ts.replace(t.getTypeId(),"");
                    if(numString.isEmpty()) {
                        throw new MojoExecutionException("The version qualifier should have the format RCn or Mn.");
                    }
                    try {
                        Integer num = Integer.valueOf(numString);
                    } catch(NumberFormatException e) {
                        throw new MojoExecutionException("The version qualifier should have the format RCn or Mn.");
                    }
                }
                return t;
            }
        }
        return null;
    }

    public static String asString() {
        String result = "";
        for(TargetType t : TargetType.values()) {
            if(!result.isEmpty())
                result += ", ";
            result += t.getTypeId();
        }
        return "[" + result + "]";
    }

}
