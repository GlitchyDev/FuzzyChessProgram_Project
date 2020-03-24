package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.GameState;

public class NullAction extends Action {
    public NullAction(GamePiece gamePiece, ActionType actionType) {
        super(gamePiece, actionType);
    }

    @Override
    public void preformAction(GameState gameState, GameBoard gameBoard) {

    }

    @Override
    public void undoAction(GameBoard gameBoard) {

    }

    @Override
    public Action clone(GameState gameState) {
        return new NullAction(getGamePiece(),getActionType());
    }
}
