package edu.school21.game;

public abstract class GameEntity {
    protected Position position;
    protected final MapData mapData;
    protected final MapElement element;

    public GameEntity(MapData mapData, MapElement element) {
        this.mapData = mapData;
        this.element = element;
        position=new Position();
        placeEntity();
    }

    protected abstract void placeEntity();

    public abstract void move();

    public Position getPosition() {
        return position;
    }


}
