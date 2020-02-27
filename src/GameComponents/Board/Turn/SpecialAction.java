package GameComponents.Board.Turn;

import GameComponents.Board.GameBoard;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;

/**
 * A Holder for a Special Movement Action ( Castling )
 */
public class SpecialAction extends MovementAction {
    private final GamePiece secondaryPiece;
    private final BoardLocation secondaryOldLocation;
    private final BoardLocation secondaryNewLocation;
    public SpecialAction(GamePiece gamePiece, ActionType actionType, BoardLocation oldLocation, BoardLocation newLocation, GamePiece secondaryPiece, BoardLocation secondaryOldLocation, BoardLocation secondaryNewLocation) {
        super(gamePiece, actionType, oldLocation, newLocation);
        this.secondaryPiece = secondaryPiece;
        this.secondaryOldLocation = secondaryOldLocation;
        this.secondaryNewLocation = secondaryNewLocation;
    }

    @Override
    public void preformAction(GameBoard gameBoard) {
        super.preformAction(gameBoard);
        gameBoard.movePiece(secondaryOldLocation,secondaryNewLocation);
        secondaryPiece.setBoardLocation(secondaryNewLocation);
    }

    @Override
    public void undoAction(GameBoard gameBoard) {
        super.undoAction(gameBoard);
        gameBoard.movePiece(secondaryNewLocation,secondaryOldLocation);
        secondaryPiece.setBoardLocation(secondaryOldLocation);
    }

    public GamePiece getSecondaryPiece() {
        return secondaryPiece;
    }

    public BoardLocation getSecondaryOldLocation() {
        return secondaryOldLocation;
    }

    public BoardLocation getSecondaryNewLocation() {
        return secondaryNewLocation;
    }
}
