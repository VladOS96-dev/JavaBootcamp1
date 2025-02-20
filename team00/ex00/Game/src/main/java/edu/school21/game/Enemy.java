package edu.school21.game;

import java.util.Random;
import edu.school21.chaseLogic.EnemyMovement;
public class Enemy extends GameEntity {
    private Random random;

    public Enemy(MapData mapData) {
        super(mapData, Settings.getInstance().getElement("enemy"));
        random = new Random();
    }

    @Override
    protected void placeEntity() {
        if (random == null) {
            random = new Random();
        }
        do {
            position.setX((int) (Math.random() * mapData.getSize())) ;
            position.setY ((int) (Math.random() * mapData.getSize()));
        } while (!mapData.isCellEmpty(position.getX(), position.getY()));
        mapData.setCell(position.getX(), position.getY(), element);
    }

    @Override
    public void move() {
        if (mapData.getPlayerPosition() == null) {
            return;
        }
        int playerX = mapData.getPlayerPosition().getX();
        int playerY = mapData.getPlayerPosition().getY();
        int enemyX = position.getX();
        int enemyY = position.getY();

        if (hasReachedPlayer()) {
            return;
        }

        char[][] charMap = convertMapDataToArray(mapData);
        int[] nextMove = EnemyMovement.getNextMove(charMap, enemyX, enemyY, playerX, playerY, mapData.getDefaultElement().getSymbol());
        int newX = enemyX + nextMove[0];
        int newY = enemyY + nextMove[1];

        if (mapData.isValidMove(newX, newY) && !mapData.getCell(newX, newY).equals(mapData.getGoalElement())) {
            mapData.setCell(position.getX(), position.getY(), mapData.getDefaultElement());
            position.setX(newX);
            position.setY(newY);
            mapData.setCell(position.getX(), position.getY(), element);
        }
    }
    private char[][] convertMapDataToArray(MapData mapData) {
        int size = mapData.getSize();
        char[][] charMap = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                charMap[i][j] = mapData.getCell(i, j).getSymbol();
            }
        }
        return charMap;
    }

    public boolean hasReachedPlayer() {
        Position playerPosition = mapData.getPlayerPosition();
        if (playerPosition == null) {
            return true;
        }
        return position.getX() == playerPosition.getX() && position.getY() == playerPosition.getY();
    }
}
