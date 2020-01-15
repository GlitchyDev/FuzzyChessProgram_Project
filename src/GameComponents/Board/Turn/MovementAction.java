package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.GameState;

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

    @Override
    public void preformAction(GameBoard gameBoard) {
        gameBoard.movePiece(oldLocation,newLocation);
        getGamePiece().setBoardLocation(newLocation);
    }

    @Override
    public void undoAction(GameBoard gameBoard) {
        gameBoard.movePiece(newLocation,oldLocation);
        getGamePiece().setBoardLocation(oldLocation);
    }


    /**
     * This Method will make a copy of the action that is actionable ( aka pointing to the right pieces ) on a new GameState
     * @param newGameState
     * @return
     */
    @Override
    public Action clone(GameState newGameState) {
        return new MovementAction(newGameState.getGameBoard().getPiece(getGamePiece().getBoardLocation()),getActionType(),oldLocation,newLocation);
    }

    @Override
    public String toString() {
        return "{MOVE " + getActionType() + " of " + getGamePiece() + " moving from " + oldLocation + " to " + newLocation + "}";
    }
}
