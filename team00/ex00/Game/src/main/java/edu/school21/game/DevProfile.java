package edu.school21.game;

public class DevProfile implements Profile {
    private final String profile;

    public DevProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String getConfigFileName() {
        return "application-dev.properties";
    }
}
