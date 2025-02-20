package edu.school21.game;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MapData {
    private final int size;
    private final MapElement[][] map;
    private final PropertyChangeSupport changes;

    public MapData(int size) {
        this.size = size;
        this.map = new MapElement[size][size];
        this.changes = new PropertyChangeSupport(this);
        clearMap();
    }

    public void setCell(int x, int y, MapElement element) {
        MapElement oldElement = map[x][y];
        map[x][y] = element;
        changes.firePropertyChange("mapUpdate", oldElement, element);
    }

    public MapElement getCell(int x, int y) {
        return map[x][y];
    }

    public int getSize() {
        return size;
    }

    public void clearMap() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = new MapElement(Settings.getInstance().getElement("empty").getSymbol(), Settings.getInstance().getElement("empty").getColor());
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }
    public boolean isValidMove(int x, int y) {
        return x >= 0 && y >= 0 && x < size && y < size
                && !getCell(x, y).equals(Settings.getInstance().getElement("wall"));
    }

    public boolean isCellEmpty(int x, int y) {
        return getCell(x, y).getSymbol() == Settings.getInstance().getElement("empty").getSymbol();
    }

    public MapElement getDefaultElement() {
        return new MapElement(Settings.getInstance().getElement("empty").getSymbol(), Settings.getInstance().getElement("empty").getColor());
    }

    public MapElement getGoalElement() {
        return  Settings.getInstance().getElement("goal");
    }
    public Position getPlayerPosition() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (getCell(i, j).equals(Settings.getInstance().getElement("player"))) {
                    return new Position(i,j);
                }
            }
        }
        return null;
    }
}
