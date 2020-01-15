package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.GameState;

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

    @Override
    public void preformAction(GameBoard gameBoard) {
        if(isSuccessful) {
            gameBoard.deletePiece(newLocation);
            gameBoard.movePiece(oldLocation, newLocation);
        }
    }

    @Override
    public void undoAction(GameBoard gameBoard) {
        if(isSuccessful) {
            gameBoard.movePiece(newLocation, oldLocation);
            gameBoard.addPiece(targetPiece, newLocation);
        }
    }

    /**
     * This Method will make a copy of the action that is actionable ( aka pointing to the right pieces ) on a new GameState
     * @param newGameState
     * @return
     */
    @Override
    public Action clone(GameState newGameState) {
        return new AttackAction(newGameState.getGameBoard().getPiece(getGamePiece().getBoardLocation()),getActionType(),newGameState.getGameBoard().getPiece(getTargetPiece().getBoardLocation()),isSuccessful);
    }

    @Override
    public String toString() {
        return "{ATTACK " + getActionType() + " of " + getGamePiece() + " at " + oldLocation + " attacking " + getTargetPiece() + " at " + newLocation + "}";
    }
}
