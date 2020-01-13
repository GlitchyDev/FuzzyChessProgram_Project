package GameComponents.Board.Turn;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;

public class MovementAction extends Action {
    private final BoardLocation oldLocation;
    private final BoardLocation newLocation;

    public MovementAction(GamePiece gamePiece, ActionType actionType, BoardLocation oldLocation, BoardLocation newLocation) {
        super(gamePiece, actionType);
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public BoardLocation getOldLocation() {
        return oldLocation;
    }

    public BoardLocation getNewLocation() {
        return newLocation;
    }
}
