package GameComponents.Board.Turn;

import GameComponents.Board.Pieces.GamePiece;

public abstract class Action {
    private final GamePiece gamePiece;
    private final ActionType actionType;

    public Action(GamePiece gamePiece, ActionType actionType) {
        this.gamePiece = gamePiece;
        this.actionType = actionType;
    }

    public GamePiece getGamePiece() {
        return gamePiece;
    }

    public ActionType getActionType() {
        return actionType;
    }
    // Piece
    // Action
}
