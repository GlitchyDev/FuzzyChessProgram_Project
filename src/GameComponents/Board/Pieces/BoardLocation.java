package GameComponents.Board.Pieces;

import GameComponents.Board.BoardDirection;

public class BoardLocation {
    private final int x;
    private final int y;

    public BoardLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BoardLocation getOffsetLocation(int xOffset, int yOffset) {
        return new BoardLocation(x + xOffset, y + yOffset);
    }

    public BoardLocation getDirectionLocation(BoardDirection boardDirection, int unit) {
        switch(boardDirection) {
            case NORTH:
                return getOffsetLocation(0 * unit, 1 * unit);
            case NORTH_EAST:
                return getOffsetLocation(1 * unit, 1 * unit);
            case EAST:
                return getOffsetLocation(1 * unit, 0 * unit);
            case SOUTH_EAST:
                return getOffsetLocation(1 * unit, -1 * unit);
            case SOUTH:
                return getOffsetLocation(0 * unit, -1 * unit);
            case SOUTH_WEST:
                return getOffsetLocation(-1 * unit, -1 * unit);
            case WEST:
                return getOffsetLocation(-1 * unit, 0 * unit);
            case NORTH_WEST:
                return getOffsetLocation(-1 * unit, 1 * unit);
        }
        return null;
    }
}
