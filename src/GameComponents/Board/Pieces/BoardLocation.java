package GameComponents.Board.Pieces;

import GameComponents.Board.BoardDirection;
import GameComponents.Board.GameBoard;

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
                return getOffsetLocation(0 * unit, -1 * unit);
            case NORTH_EAST:
                return getOffsetLocation(1 * unit, -1 * unit);
            case EAST:
                return getOffsetLocation(1 * unit, 0 * unit);
            case SOUTH_EAST:
                return getOffsetLocation(1 * unit, 1 * unit);
            case SOUTH:
                return getOffsetLocation(0 * unit, 1 * unit);
            case SOUTH_WEST:
                return getOffsetLocation(-1 * unit, 1 * unit);
            case WEST:
                return getOffsetLocation(-1 * unit, 0 * unit);
            case NORTH_WEST:
                return getOffsetLocation(-1 * unit, -1 * unit);
        }
        return null;
    }

    public BoardDirection getDirection(BoardLocation boardLocation) {
        BoardDirection closestDirection = null;
        double closestDistance = Double.MAX_VALUE;
        for(BoardDirection direction: BoardDirection.values()) {
            double distance = boardLocation.getDistance(getDirectionLocation(direction,1));
            if(distance < closestDistance) {
                closestDirection = direction;
                closestDistance = distance;
            }
        }
        return closestDirection;
    }

    public boolean matchesDirection(BoardLocation boardLocation, BoardDirection boardDirection) {
        for(int i = 1; i <= 10; i++) {
            BoardLocation testLocation = getDirectionLocation(boardDirection,i);
            if(testLocation.getX() == boardLocation.getX() && testLocation.getY() == boardLocation.getY()) {
                return true;
            }
        }
        return false;
    }


    public double getDistance(BoardLocation boardLocation) {
        double xDifference = x - boardLocation.getX();
        double yDifference = y - boardLocation.getY();
        return Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2));
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public String toFormal() {
        String returnString = "(";
        switch(x) {
            case 0:
                returnString += "a";
                break;
            case 1:
                returnString += "b";
                break;
            case 2:
                returnString += "c";
                break;
            case 3:
                returnString += "d";
                break;
            case 4:
                returnString += "e";
                break;
            case 5:
                returnString += "f";
                break;
            case 6:
                returnString += "g";
                break;
            case 7:
                returnString += "h";
                break;
        }
        returnString += 8-y;
        returnString += ")";
        return returnString;
    }
}
