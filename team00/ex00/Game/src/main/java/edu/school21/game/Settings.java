package edu.school21.game;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static Settings instance;
    private final Map<String, MapElement> elements = new HashMap<>();

    private Settings(Path resourceFile) {
        loadSettings(resourceFile);
    }

    public static Settings getInstance() {
        return instance;
    }

    public static void initialize(Path resourceFile) {
        if (instance == null) {
            instance = new Settings(resourceFile);
        } else {
            throw new IllegalStateException("Settings already initialized.");
        }
    }

    private void loadSettings(Path resourceFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(resourceFile.toString()))) {
            Map<String, String> symbols = new HashMap<>();
            Map<String, Color> colors = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("=");
                if (tokens.length == 2) {
                    String key = tokens[0].trim();
                    String value = tokens[1].trim();

                    if (key.endsWith(".char")) {
                        symbols.put(key.replace(".char", ""), value.isEmpty() ? " " : value);
                    } else if (key.endsWith(".color")) {
                        try {
                            colors.put(key.replace(".color", ""), (Color) Color.class.getField(value).get(null));
                        } catch (Exception e) {
                            colors.put(key.replace(".color", ""), Color.WHITE);
                        }
                    }
                }
                else if(tokens.length == 1)
                {
                    String key = tokens[0].trim();
                    if (key.endsWith(".char")) {
                        symbols.put(key.replace(".char", "")," " );

                    }
                }
            }

            for (String key : symbols.keySet()) {
                elements.put(key, new MapElement(symbols.get(key).charAt(0), colors.getOrDefault(key, Color.WHITE)));
            }
        } catch (IOException e) {
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            throw new RuntimeException("Error loading settings file: " + e.getMessage());
        }
    }

    public MapElement getElement(String key) {
        return elements.getOrDefault(key, new MapElement(' ', Color.WHITE));
    }
}
