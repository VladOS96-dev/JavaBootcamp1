package edu.school21.game;

public class CustomProfile implements Profile {
    private final String profile;

    public CustomProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String getConfigFileName() {
        return "application-" + profile + ".properties";
    }
}
