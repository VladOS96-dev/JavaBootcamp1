package edu.school21.game;

import java.nio.file.Path;
import java.util.Scanner;
import com.beust.jcommander.JCommander;

public class Game {
    public static void main(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Usage: java -jar game.jar --enemiesCount=<number> --wallsCount=<number> --size=<number> --profile=<string>");
        }
        ArgsParser argsParser = ArgsParser.getInstance();
        JCommander jCommander = JCommander.newBuilder().addObject(argsParser).build();
        try {
            jCommander.parse(args);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid argument");
        }

        int enemiesCount = argsParser.getEnemiesCount();
        int wallsCount = argsParser.getWallsCount();
        int size = argsParser.getSize();
        Profile profile = ProfileFactory.createProfile(argsParser.getProfile());
        Path resourceFile = FileFinder.findFile(profile.getConfigFileName());

        if (enemiesCount < 1 || wallsCount < 1 || size <= 2) {
            throw new IllegalParametersException("Invalid input: enemiesCount, wallsCount must be > 0, size must be > 2.");
        }

        if (enemiesCount + wallsCount >= size * size - 1) {
            throw new IllegalParametersException("Too many obstacles or enemies for the given map size.");
        }

        Settings.initialize(resourceFile);
        MapData mapData = new MapData(size);
        MapGeneration generator = new MapGeneration(size, wallsCount, enemiesCount, mapData);
        Renderer renderer = new Renderer(mapData);

        generator.generate();
        Player player = generator.getPlayer();
        Enemy[] enemies = generator.getEnemies();

        renderer.draw();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            player.move();
            if (argsParser.getProfile().equals("dev")) {
                renderer.draw();
                for (Enemy enemy : enemies) {
                    if (enemy.hasReachedPlayer()) {
                        break;
                    }
                    System.out.print("Press 8 to move the enemy: ");
                    while (!scanner.nextLine().equals("8")) {
                        System.out.print("Invalid input. Press 8 to move the enemy: ");
                    }
                    enemy.move();
                    renderer.draw();
                }
            } else {
                for (Enemy enemy : enemies) {
                    enemy.move();
                    if (enemy.hasReachedPlayer()) {
                        break;
                    }
                }
                renderer.draw();
            }

            for (Enemy enemy : enemies) {
                if (enemy.hasReachedPlayer() && !player.hasReachedGoal()) {
                    System.out.println("Game Over! You were caught by the enemies.");
                    return;
                }
            }

            if (player.hasReachedGoal()) {
                System.out.println("Congratulations! You've reached the goal!");
                break;
            }

            if (!canPlayerMove(player, mapData)) {
                System.out.println("Game Over! You are surrounded.");
                break;
            }
        }
    }

    private static boolean canPlayerMove(Player player, MapData mapData) {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        return mapData.isValidMove(x + 1, y) || mapData.isValidMove(x - 1, y) ||
                mapData.isValidMove(x, y + 1) || mapData.isValidMove(x, y - 1);
    }
}
