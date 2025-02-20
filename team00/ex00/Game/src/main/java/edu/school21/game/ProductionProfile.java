package edu.school21.game;

public class ProductionProfile implements Profile {
    private final String profile;

    public ProductionProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String getConfigFileName() {
        return "application-production.properties";
    }
}