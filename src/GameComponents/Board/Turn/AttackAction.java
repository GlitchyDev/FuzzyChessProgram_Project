package GameComponents.Board.Turn;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;

public class AttackAction extends Action {
    private final GamePiece targetPiece;
    private final boolean isSuccessful;
    private final BoardLocation oldLocation;
    private final BoardLocation newLocation;

    // Successful Attack
    public AttackAction(GamePiece gamePiece, ActionType actionType, GamePiece targetPiece, boolean isSuccessful) {
        super(gamePiece, actionType);
        this.targetPiece = targetPiece;
        this.isSuccessful = isSuccessful;
        this.oldLocation = gamePiece.getBoardLocation();
        this.newLocation = targetPiece.getBoardLocation();
    }

    public GamePiece getTargetPiece() {
        return targetPiece;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public BoardLocation getOldLocation() {
        return oldLocation;
    }

    public BoardLocation getNewLocation() {
        return newLocation;
    }
}
