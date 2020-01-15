package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.GamePiece;

public abstract class Action {
    private GamePiece gamePiece;
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

    public abstract void preformAction(GameBoard gameBoard);
    public abstract void undoAction(GameBoard gameBoard);

    public void branchSubstitute(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }

    // Piece
    // Action
}
