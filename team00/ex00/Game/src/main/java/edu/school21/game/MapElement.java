package edu.school21.game;

import java.awt.*;

public class MapElement {
    private final char symbol;
    private final Color color;

    public MapElement(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    public char getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }
}
