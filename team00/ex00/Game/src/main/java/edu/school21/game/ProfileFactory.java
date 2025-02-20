package edu.school21.game;

public class ProfileFactory {
    public static Profile createProfile(String profile) {
        if (profile.equals("dev")) {
            return new DevProfile(profile);
        } else if (profile.equals("production")) {
            return new ProductionProfile(profile);
        } else {
            return new CustomProfile(profile);
        }
    }
}

