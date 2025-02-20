package edu.school21.game;

import java.util.Scanner;

public class Player extends GameEntity {
    private final Scanner scanner;
    public Player(MapData mapData) {
        super(mapData, Settings.getInstance().getElement("player"));
        this.scanner = new Scanner(System.in);
    }

    @Override
    protected void placeEntity() {
        do {
            position.setX((int) (Math.random() * mapData.getSize())) ;
            position.setY ((int) (Math.random() * mapData.getSize()));
        } while (!mapData.isCellEmpty(position.getX(), position.getY()));
        mapData.setCell(position.getX(), position.getY(), element);
    }

    @Override
    public void move() {
        while (true) {
            int newX = position.getX(), newY = position.getY();
            System.out.print("Enter command (w/a/s/d or 9 for exit): ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "w" -> newX--;
                case "s" -> newX++;
                case "a" -> newY--;
                case "d" -> newY++;
                case "9" -> {
                    System.out.println("You have exited the game.");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Invalid command. Try again.");
                    continue;
                }
            }

            if (mapData.isValidMove(newX, newY)) {
                mapData.setCell(position.getX(), position.getY(), mapData.getDefaultElement());
                position.setX(newX);
                position.setY(newY);
                if (hasReachedGoal()) {
                    break;
                }
                mapData.setCell(position.getX(), position.getY(), element);
                break;
            } else {
                System.out.println("Impossible to move in chosen direction.");
            }
        }
    }

    public boolean hasReachedGoal() {
        return mapData.getCell(position.getX(), position.getY()).equals(mapData.getGoalElement());
    }
}
