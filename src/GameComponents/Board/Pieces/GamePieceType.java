package GameComponents.Board.Pieces;

import GameComponents.Board.Turn.ActionType;

import java.util.ArrayList;

public enum GamePieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;


    public ArrayList<ActionType> getAvalibleActions() {
        ArrayList<ActionType> availableActions = new ArrayList<>();
        availableActions.add(ActionType.MOVEMENT_STANDARD);
        switch(this) {
            case PAWN:
                availableActions.add(ActionType.MOVEMENT_PAWN_ADVANCE);
                availableActions.add(ActionType.ATTACK_PAWN);
                availableActions.add(ActionType.ATTACK_PAWN_EN_PASSANT);
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
                availableActions.add(ActionType.MOVEMENT_CASTLE_ROOK);
                break;
            case QUEEN:
                availableActions.add(ActionType.MOVEMENT_QUEEN);
                availableActions.add(ActionType.ATTACK_QUEEN);
                break;
            case KING:
                availableActions.add(ActionType.ATTACK_KNIGHT);
                availableActions.add(ActionType.ATTACK_KING);
                break;
        }

        return availableActions;
    }


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
