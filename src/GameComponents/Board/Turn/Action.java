package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.GameState;

/**
 * A class that represents any given action that can be taken on a Piece
 */
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

    /**
     * Preforms the given action to the GameBoard provided
     * @param gameState
     * @param gameBoard
     */
    public abstract void preformAction(GameState gameState, GameBoard gameBoard);

    /**
     * Undo's the given action to the GameBoard provided
     * @param gameBoard
     */
    public abstract void undoAction(GameBoard gameBoard);

    public abstract Action clone(GameState gameState);

    // Piece
    // Action
}
