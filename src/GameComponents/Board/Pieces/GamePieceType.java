package GameComponents.Board.Pieces;

import GameComponents.Board.Turn.ActionType;

import java.util.ArrayList;

/**
 * An Enum that contains all logic and code that differentiates all Gamepieces types from each other
 */
public enum GamePieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;


    public ArrayList<ActionType> getAvailableActions() {
        ArrayList<ActionType> availableActions = new ArrayList<>();
        availableActions.add(ActionType.MOVEMENT_STANDARD);
        switch(this) {
            case PAWN:
                availableActions.add(ActionType.MOVEMENT_PAWN_ADVANCE);
                availableActions.add(ActionType.ATTACK_PAWN);
                break;
            case KNIGHT:
                availableActions.add(ActionType.MOVEMENT_KNIGHT);
                availableActions.add(ActionType.ATTACK_KNIGHT);
                break;
            case BISHOP:
                availableActions.add(ActionType.MOVEMENT_BISHOP);
                availableActions.add(ActionType.ATTACK_BISHOP);
                break;
            case ROOK:
                availableActions.add(ActionType.MOVEMENT_ROOK);
                availableActions.add(ActionType.ATTACK_ROOK);
                break;
            case QUEEN:
                availableActions.add(ActionType.MOVEMENT_QUEEN);
                availableActions.add(ActionType.ATTACK_QUEEN);
                break;
            case KING:
                availableActions.add(ActionType.ATTACK_KNIGHT);
                availableActions.add(ActionType.ATTACK_KING);
                availableActions.add(ActionType.CASTLING);
                break;
        }

        return availableActions;
    }

    // If the Piece can Attack Twice
    public boolean canAttackTwice() {
        switch(this) {
            case KNIGHT:
                return true;
            case PAWN:
            case BISHOP:
            case ROOK:
            case QUEEN:
            case KING:
                return false;
        }
        return Boolean.parseBoolean(null);
    }

    // If the Piece can move Twice
    public boolean canMoveTwice() {
        switch(this) {
            case BISHOP:
            case ROOK:
            case QUEEN:
                return true;
            case PAWN:
            case KNIGHT:
            case KING:
                return false;
        }
        return Boolean.parseBoolean(null);
    }
}
