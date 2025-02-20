package edu.school21.game;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=")
public class ArgsParser {
    private static ArgsParser instance;

    @Parameter(names = "--enemiesCount", description = "Number of enemies", required = true)
    private int enemiesCount;

    @Parameter(names = "--wallsCount", description = "Number of walls", required = true)
    private int wallsCount;

    @Parameter(names = "--size", description = "Size of the window", required = true)
    private int size;

    @Parameter(names = "--profile", description = "Mode parameter", required = true)
    private String profile;

    private ArgsParser() {}

    public static ArgsParser getInstance() {
        if (instance == null) {
            instance = new ArgsParser();
        }
        return instance;
    }

    public int getEnemiesCount() {
        return enemiesCount;
    }

    public int getWallsCount() {
        return wallsCount;
    }

    public int getSize() {
        return size;
    }

    public String getProfile() {
        return profile;
    }

}