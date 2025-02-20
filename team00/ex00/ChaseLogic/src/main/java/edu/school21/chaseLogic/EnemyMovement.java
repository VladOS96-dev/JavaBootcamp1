package edu.school21.chaseLogic;

public class EnemyMovement {
    public static int[] getNextMove(char[][] map, int startX, int startY, int targetX, int targetY, char emptyCell) {
        int dx = 0;
        int dy = 0;

        if ((Math.abs(startX - targetX) == 1 && startY == targetY) ||
                (Math.abs(startY - targetY) == 1 && startX == targetX)){
            dx = Integer.compare(targetX, startX);
            dy = Integer.compare(targetY, startY);
            return new int[]{dx, dy};
        }

        if (startX > 0 && map[startX - 1][startY] == emptyCell && (targetX < startX)) {
            dx = -1;
        }
        else if (startX < map.length -1 && map[startX + 1][startY] == emptyCell && (targetX > startX)) {
            dx = 1;
        }
        else if (startY > 0 && map[startX][startY - 1] == emptyCell && (targetY < startY)) {
            dy = -1;
        }
        else if (startY < map[0].length - 1 && map[startX][startY + 1] == emptyCell && (targetY > startY)) {
            dy = 1;
        }
        return new int[]{dx, dy};
    }
}