package GameComponents;

import GameComponents.Board.BoardDirection;
import GameComponents.Board.GameBoard;
import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.Board.Turn.ActionType;
import GameComponents.Board.Turn.AttackAction;
import GameComponents.Board.Turn.MovementAction;

import java.util.ArrayList;
import java.util.Random;

/**
 * A Wrapper Object that holds the GameBoard, and tracks all actions done. It allows for easy branching and cloning to show all possibilities
 */
public class GameState {
    private final int ACTIONS_PER_TURN = 2;
    private GameBoard gameBoard;
    private GameTeam currentTeamTurn = GameTeam.WHITE;
    private ArrayList<Action> pastActions;
    private int currentTurnNumber;



    public GameState() {
        this.gameBoard = new GameBoard();
        this.pastActions = new ArrayList<>();
        this.currentTurnNumber = 1;
    }

    /**
     * A constructor specifically used to create a Clone GameState from a Donor GameState and a Branching Action
     * @param originalGameState
     * @param branchingAction
     */
    public GameState(GameState originalGameState, Action branchingAction) {
        this.gameBoard = originalGameState.getGameBoard().clone();
        this.currentTeamTurn = originalGameState.getCurrentTeamTurn();
        this.pastActions = new ArrayList<>();
        for(Action action: originalGameState.getPastActions()) {
            pastActions.add(action);
        }
        this.currentTurnNumber = originalGameState.getCurrentTurnNumber();
        preformAction(branchingAction.clone(this));
    }


    /**
     * Retrieve all Actions taken at the specified Turn Number
     * @param turnNumber
     * @return
     */
    public ArrayList<Action> getTurnActions(int turnNumber) {
        ArrayList<Action> turnsActions = new ArrayList<>();
        int actionOffset = (turnNumber-1) * 2;
        if(pastActions.size() > actionOffset) {
            turnsActions.add(pastActions.get(actionOffset));
            if(pastActions.size() > actionOffset + 1) {
                turnsActions.add(pastActions.get(actionOffset + 1));
            }
        }
        return turnsActions;
    }

    /**
     * Return all VALID ( Can do ) Actions this piece can do on the provided Board
     * @param gamePiece Piece
     * @return All Valid Actions
     */
    public ArrayList<Action> getValidActions(GamePiece gamePiece) {
        ArrayList<Action> validActions = new ArrayList<>();
        ArrayList<ActionType> availableActions = gamePiece.getGamePieceType().getAvalibleActions();


        if(gamePiece.getGameTeam() == getCurrentTeamTurn()) {
            for(ActionType actionType: availableActions) {
                // Check if this piece is eligible to preform a given action
                if (actionType.isAttack()) {
                    if (!hasPiecePreviouslyAttacked(gamePiece) || gamePiece.getGamePieceType().canAttackTwice()) {
                        generateValidActions(gamePiece, actionType, validActions);
                    }
                }
                if (actionType.isMovement()) {
                    if (!hasPiecePreviouslyMoved(gamePiece) || gamePiece.getGamePieceType().canMoveTwice()) {
                        generateValidActions(gamePiece, actionType, validActions);
                    }
                }
            }

        }
        return validActions;
    }

    /**
     * Method that actually adds the valid actions to the ValidAction List, helper seperator method.
     * This checks if the move is PHYSICALLY possible
     * @param gamePiece
     * @param actionType
     * @param validActions
     */
    private void generateValidActions(GamePiece gamePiece, ActionType actionType, ArrayList<Action> validActions) {
        switch(actionType) {
            case MOVEMENT_STANDARD:
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                        if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                            validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_STANDARD,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)));
                        }
                    }
                }
                break;
            case MOVEMENT_PAWN_ADVANCE:
                int offset = gamePiece.getGameTeam() == GameTeam.WHITE ? -1 : 1;
                boolean pass = true;
                for(Action action: pastActions) {
                    if(action.getGamePiece().equals(gamePiece)) {
                        pass = false;
                    }
                }
                if(pass) {
                    if (gameBoard.isInsideBoard(gamePiece.getBoardLocation().getOffsetLocation(0, offset))) {
                        if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getOffsetLocation(0, offset))) {
                            if (gameBoard.isInsideBoard(gamePiece.getBoardLocation().getOffsetLocation(0, offset * 2))) {
                                if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getOffsetLocation(0, offset * 2))) {
                                    validActions.add(new MovementAction(gamePiece, ActionType.MOVEMENT_PAWN_ADVANCE, gamePiece.getBoardLocation(), gamePiece.getBoardLocation().getOffsetLocation(0, offset * 2)));
                                }
                            }
                        }
                    }
                }
                break;
            case ATTACK_PAWN_EN_PASSANT:
                // TODO See if teacher is requring it, if so check the Turn Log for last turn, see if a pawn advanced, if the current piece is in the right now
                // If the pawn in question is in a valid position
                break;
            case ATTACK_PAWN:
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                        if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)).getGameTeam() != gamePiece.getGameTeam()) {
                            validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_PAWN,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)), true));
                        }
                    }
                }
                break;
            case MOVEMENT_KNIGHT:
                final BoardLocation[][] knightMovements = new BoardLocation[][]{
                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,1),gamePiece.getBoardLocation().getOffsetLocation(0,2),gamePiece.getBoardLocation().getOffsetLocation(1, 2)},
                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,1),gamePiece.getBoardLocation().getOffsetLocation(0,2),gamePiece.getBoardLocation().getOffsetLocation(-1, 2)},

                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,-1),gamePiece.getBoardLocation().getOffsetLocation(0,-2),gamePiece.getBoardLocation().getOffsetLocation(1,-2)},
                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,-1),gamePiece.getBoardLocation().getOffsetLocation(0,-2),gamePiece.getBoardLocation().getOffsetLocation(-1,-2)},

                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(1,0),gamePiece.getBoardLocation().getOffsetLocation(2,0),gamePiece.getBoardLocation().getOffsetLocation(2,1)},
                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(1,0),gamePiece.getBoardLocation().getOffsetLocation(2,0),gamePiece.getBoardLocation().getOffsetLocation(2,-1)},

                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(-1,0),gamePiece.getBoardLocation().getOffsetLocation(-2,0),gamePiece.getBoardLocation().getOffsetLocation(-2,1)},
                        new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(-1,0),gamePiece.getBoardLocation().getOffsetLocation(-2,0),gamePiece.getBoardLocation().getOffsetLocation(-2,-1)},
                };


                for(int i = 0; i < knightMovements.length; i++) {
                    if(gameBoard.isInsideBoard(knightMovements[i][2])) {
                        if (!gameBoard.isSpaceOccupied(knightMovements[i][2])) {
                            validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_KNIGHT,gamePiece.getBoardLocation(),knightMovements[i][2]));
                        }
                    }
                }
                break;
            case ATTACK_KNIGHT:
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                        if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)).getGameTeam() != gamePiece.getGameTeam()) {
                            validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_KNIGHT,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)), true));
                        }
                    }
                }
                break;
            case MOVEMENT_BISHOP:
                final int BISHOP_MOVEMENT_LENGTH = 4;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    int passCount = 0;
                    for (int i = 1; i <= BISHOP_MOVEMENT_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                passCount++;
                            }
                        }
                    }
                    if(passCount == BISHOP_MOVEMENT_LENGTH) {
                        validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_BISHOP,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,BISHOP_MOVEMENT_LENGTH)));
                    }
                }
                break;
            case ATTACK_BISHOP:
                final int BISHOP_ATTACK_LENGTH = 4;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    int passCount = 0;
                    for (int i = 1; i <= BISHOP_ATTACK_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(i != BISHOP_ATTACK_LENGTH) {
                                if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                    passCount++;
                                }
                            } else {
                                if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)).getGameTeam() != gamePiece.getGameTeam()) {
                                    passCount++;
                                }
                            }
                        }
                    }
                    if(passCount == BISHOP_ATTACK_LENGTH) {
                        validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_BISHOP,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,BISHOP_ATTACK_LENGTH)), true));
                    }
                }
                break;
            case MOVEMENT_ROOK:
                final int ROOK_MOVEMENT_LENGTH = 3;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    int passCount = 0;
                    for (int i = 1; i <= ROOK_MOVEMENT_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                passCount++;
                            }
                        }
                    }
                    if(passCount == ROOK_MOVEMENT_LENGTH) {
                        validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_ROOK,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,ROOK_MOVEMENT_LENGTH)));
                    }
                }
                break;
            case MOVEMENT_CASTLE_ROOK:
                // TODO add Castling Logic
                break;
            case ATTACK_ROOK:
                final int ROOK_ATTACK_LENGTH = 3;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    int passCount = 0;
                    for (int i = 1; i <= ROOK_ATTACK_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(i != ROOK_ATTACK_LENGTH) {
                                if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                    passCount++;
                                }
                            } else {
                                if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)).getGameTeam() != gamePiece.getGameTeam()) {
                                    passCount++;
                                }
                            }
                        }
                    }
                    if(passCount == ROOK_ATTACK_LENGTH) {
                        validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_ROOK,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,ROOK_ATTACK_LENGTH)), true));
                    }
                }
                break;
            case MOVEMENT_QUEEN:
                final int QUEEN_MOVEMENT_LENGTH = 5;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    int passCount = 0;
                    for (int i = 1; i <= QUEEN_MOVEMENT_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                passCount++;
                            }
                        }
                    }
                    if(passCount == QUEEN_MOVEMENT_LENGTH) {
                        validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_ROOK,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,QUEEN_MOVEMENT_LENGTH)));
                    }
                }
                break;
            case ATTACK_QUEEN:
                final int QUEEN_ATTACK_LENGTH = 3;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    int passCount = 0;
                    for (int i = 1; i <= QUEEN_ATTACK_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(i != QUEEN_ATTACK_LENGTH) {
                                if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                    passCount++;
                                }
                            } else {
                                if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)).getGameTeam() != gamePiece.getGameTeam()) {
                                    passCount++;
                                }
                            }
                        }
                    }
                    if(passCount == QUEEN_ATTACK_LENGTH) {
                        validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_QUEEN,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,QUEEN_ATTACK_LENGTH)), true));
                    }
                }
                break;
            case MOVEMENT_CASTLE_KING:
                // Todo Add Castling Logic
                break;
            case ATTACK_KING:
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                        if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)).getGameTeam() != gamePiece.getGameTeam()) {
                            validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_KING,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)), false));
                        }
                    }
                }
                break;
        }
    }


    public boolean hasPiecePreviouslyAttacked(GamePiece gamePiece) {
        ArrayList<Action> currentTurnsActions = getTurnActions(currentTurnNumber);
        if(currentTurnsActions.size() != 0) {
            for (Action action : currentTurnsActions) {
                if (action.getGamePiece().equals(gamePiece)) {
                    if (action.getActionType().isAttack()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasPiecePreviouslyMoved(GamePiece gamePiece) {
        ArrayList<Action> currentTurnsActions = getTurnActions(currentTurnNumber);
        if(currentTurnsActions.size() != 0) {
            for (Action action : currentTurnsActions) {
                if (action.getGamePiece().equals(gamePiece)) {
                    if (action.getActionType().isMovement()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Executes the current action, and modifies the turn counter as required
     * @param action
     */
    public void preformAction(Action action) {
        action.preformAction(gameBoard);
        pastActions.add(action);
        if(pastActions.size() % ACTIONS_PER_TURN == 0) {
            switchTurn();
        }
    }

    /**
     * Undo's the action, it will only work if this action is back-front linear
     * @param action
     */
    public void undoAction(Action action) {
        action.undoAction(gameBoard);
        pastActions.remove(action);
        if(pastActions.size() % ACTIONS_PER_TURN == 0) {
            switchTurn();
        }
    }

    private void switchTurn() {
        currentTeamTurn = getNextTeamTurn();
        currentTurnNumber++;
    }

    public GameTeam getNextTeamTurn() {
        switch(currentTeamTurn) {
            case BLACK:
                return GameTeam.WHITE;
            case WHITE:
                return GameTeam.BLACK;
            default:
        return null;
        }
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GameTeam getCurrentTeamTurn() {
        return currentTeamTurn;
    }

    public ArrayList<Action> getPastActions() {
        return pastActions;
    }

    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }

    public GameState branchState(Action branchingAction) {
        return new GameState(this,branchingAction);
    }

    @Override
    public String toString() {
        return "GameState: Turn: " + currentTurnNumber + "\n" + gameBoard;

    }
}
