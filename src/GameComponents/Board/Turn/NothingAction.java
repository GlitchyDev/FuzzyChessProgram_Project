package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.GameState;

public class NothingAction extends Action {
    public NothingAction(GamePiece gamePiece) {
        super(gamePiece, ActionType.NOTHING);
    }

    @Override
    public void preformAction(GameState gameState, GameBoard gameBoard) {

    }

    @Override
    public void undoAction(GameBoard gameBoard) {

    }

    @Override
    public Action clone(GameState gameState) {
        return new NothingAction(getGamePiece());
    }
}
