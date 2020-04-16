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
                //availableActions.add(ActionType.CASTLING);
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

    public double getSuccessAttackChance(GamePieceType attackTarget) {
        int successes = 0;
        for(int i = 1; i <= 6; i++) {
            if(isSuccessfulAttackRoll(attackTarget,i)) {
                successes++;
            }
        }
        return 1.0/6.0*successes;
    }

    public int requiredRoll(GamePieceType attackTarget) {
        for(int i = 1; i <= 6; i++) {
            if(isSuccessfulAttackRoll(attackTarget,i)) {
                return i;
            }
        }
        return -1;
    }

    public boolean isSuccessfulAttackRoll(GamePieceType attackTarget, int attackRoll) {
        switch(this) {
            case KING:
            case QUEEN:
                switch(attackTarget) {
                    case KING:
                    case QUEEN:
                        return attackRoll >= 4;
                    case BISHOP:
                    case ROOK:
                        return attackRoll >= 3;
                    case KNIGHT:
                        return attackRoll >= 2;
                    case PAWN:
                        return true;
                }
                break;
            case BISHOP:
            case ROOK:
                switch(attackTarget) {
                    case KING:
                    case QUEEN:
                        return attackRoll >= 5;
                    case BISHOP:
                    case ROOK:
                        return attackRoll >= 4;
                    case KNIGHT:
                        return attackRoll >= 3;
                    case PAWN:
                        return attackRoll >= 2;
                }
                break;
            case KNIGHT:
                switch(attackTarget) {
                    case KING:
                    case QUEEN:
                        return attackRoll >= 6;
                    case BISHOP:
                    case ROOK:
                        return attackRoll >= 5;
                    case KNIGHT:
                        return attackRoll >= 4;
                    case PAWN:
                        return attackRoll >= 3;
                }
                break;
            case PAWN:
                switch(attackTarget) {
                    case KING:
                    case QUEEN:
                        return attackRoll >= 6;
                    case BISHOP:
                    case ROOK:
                        return attackRoll >= 6;
                    case KNIGHT:
                        return attackRoll >= 5;
                    case PAWN:
                        return attackRoll >= 4;
                }
                break;
        }
        System.out.println("ERROR ERROR! Attack roll unresolved! Error!");
        return false;
    }
}
