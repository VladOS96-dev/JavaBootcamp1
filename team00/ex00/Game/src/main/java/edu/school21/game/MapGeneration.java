package edu.school21.game;

import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class MapGeneration {
    private final int size;
    private final int wallsCount;
    private final int enemiesCount;
    private final MapData mapData;
    private final Random random = new Random();
    private Player player;
    private Enemy[] enemies;
    public MapGeneration(int size, int wallsCount, int enemiesCount, MapData mapData) {
        this.size = size;
        this.wallsCount = wallsCount;
        this.enemiesCount = enemiesCount;
        this.mapData = mapData;

    }

    private boolean canReachGoal(int startX, int startY, int goalX, int goalY) {
        Set<String> visited = new HashSet<>();
        return dfs(startX, startY, goalX, goalY, visited);
    }

    private boolean dfs(int x, int y, int goalX, int goalY, Set<String> visited) {
        if (x == goalX && y == goalY) return true;
        if (x < 0 || x >= size || y < 0 || y >= size || !mapData.isValidMove(x, y) || visited.contains(x + "," + y)) return false;

        visited.add(x + "," + y);
        return dfs(x + 1, y, goalX, goalY, visited) ||
                dfs(x - 1, y, goalX, goalY, visited) ||
                dfs(x, y + 1, goalX, goalY, visited) ||
                dfs(x, y - 1, goalX, goalY, visited);
    }

    public void generate() {
        do {
            mapData.clearMap();
            placeElements(wallsCount, Settings.getInstance().getElement("wall"));
            enemies = new Enemy[enemiesCount];
            for (int i = 0; i < enemiesCount; i++) {
                enemies[i] = new Enemy(mapData);
            }
            player = new Player(mapData);
            placeSingleElement(Settings.getInstance().getElement("goal"));
        } while (!isMapSolvable());
    }

    private boolean isMapSolvable() {
        Position playerPos = player.getPosition();
        Position goalPos = findGoalPosition();

        if(goalPos == null || playerPos == null) return false;

        return canReachGoal(playerPos.getX(), playerPos.getY(), goalPos.getX(), goalPos.getY());
    }

    private Position findGoalPosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (mapData.getCell(i, j).equals(mapData.getGoalElement())) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }
    private void placeElements(int count, MapElement element) {
        for (int i = 0; i < count; i++) {
            int x, y;
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (isCellEmpty(x,y));
            mapData.setCell(x, y, element);
        }
    }

    private void placeSingleElement(MapElement element) {
        int x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (isCellEmpty(x,y));
        mapData.setCell(x, y, element);
    }

    private boolean isCellEmpty(int x, int y) {
        return mapData.getCell(x, y).getSymbol() != Settings.getInstance().getElement("empty").getSymbol();
    }
}
