package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.GameState;

/**
 * A Type of Action that concerns attacking
 */
public class AttackAction extends Action {
    private final GamePiece targetPiece;
    private boolean forceSuccess;
    private boolean isSuccessful;
    private final BoardLocation oldLocation;
    private final BoardLocation newLocation;

    // Successful Attack
    public AttackAction(GamePiece gamePiece, ActionType actionType, GamePiece targetPiece, boolean forceSuccess) {
        super(gamePiece, actionType);
        this.targetPiece = targetPiece;
        this.forceSuccess = forceSuccess;
        this.isSuccessful = false;
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
    public void preformAction(GameState gameState, GameBoard gameBoard) {
        if (!forceSuccess) {
            int attackRoll = gameState.getDieRoll(getGamePiece(), getTargetPiece());
            System.out.println("Gamepiece " + getGamePiece() + " attacking " + getTargetPiece() + " with a roll of " + attackRoll);
            isSuccessful = getGamePiece().getGamePieceType().isSuccessfulAttackRoll(targetPiece.getGamePieceType(), attackRoll);
         } else {
            System.out.println("Forced Success!");
            isSuccessful = true;
        }
        System.out.println("Attack Successful: " + isSuccessful);
        // Here do currentResult = getDieRoll(); shit and figure out if stuff is successful
        if(isSuccessful) {
            gameBoard.deletePiece(newLocation);
            gameBoard.movePiece(oldLocation, newLocation);
            getGamePiece().setBoardLocation(newLocation);
        }
    }

    @Override
    public void undoAction(GameBoard gameBoard) {
        if(isSuccessful) {
            gameBoard.movePiece(newLocation, oldLocation);
            gameBoard.addPiece(targetPiece, newLocation);
            getGamePiece().setBoardLocation(oldLocation);
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
