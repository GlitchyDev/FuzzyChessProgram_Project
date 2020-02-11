package GameComponents;

import GameComponents.Board.BoardDirection;
import GameComponents.Board.GameBoard;
import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;
import GameComponents.Board.Turn.*;
import GameComponents.Controllers.AIController;

import java.util.ArrayList;

/**
 * A Wrapper Object that holds the GameBoard, and tracks all actions done. It allows for easy branching and cloning to show all possibilities
 */
public class GameState {
    private final int ACTIONS_PER_TURN = 2;
    private GameBoard gameBoard;
    private AIController aiController;
    private GameTeam currentTeamTurn = GameTeam.WHITE;
    private ArrayList<Action> pastActions;
    private int currentTurnNumber;
    private boolean useAIMode = false;



    public GameState(AIController aiController) {
        this.gameBoard = new GameBoard();
        this.aiController = aiController;
        this.pastActions = new ArrayList<>();
        this.currentTurnNumber = 1;
    }

    /**
     * A constructor specifically used to create a Clone GameState from a Donor GameState and a Branching Action
     * @param originalGameState
     * @param branchingAction
     */
    public GameState(GameState originalGameState, Action branchingAction, AIController aiController) {
        this.gameBoard = originalGameState.getGameBoard().clone();
        this.aiController = aiController;
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
                    if (!hasPiecePreviouslyAttackedThisTurn(gamePiece) || gamePiece.getGamePieceType().canAttackTwice()) {
                        generateValidActions(gamePiece, actionType, validActions);
                    }
                }
                if (actionType.isMovement()) {
                    if (!hasPiecePreviouslyMovedThisTurn(gamePiece) || gamePiece.getGamePieceType().canMoveTwice()) {
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
                if(!hasPiecePreviouslyAttacked(gamePiece) && !hasPiecePreviouslyMoved(gamePiece)) {
                    for(BoardDirection boardDirection: BoardDirection.values()) {
                        if (gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, 1))) {
                            if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, 1))) {
                                if (gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, 2))) {
                                    if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, 2))) {
                                        validActions.add(new MovementAction(gamePiece, ActionType.MOVEMENT_PAWN_ADVANCE, gamePiece.getBoardLocation(), gamePiece.getBoardLocation().getDirectionLocation(boardDirection, 2)));
                                    }
                                }
                            }
                        }

                    }
                }
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
                    for (int i = 1; i <= BISHOP_MOVEMENT_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_BISHOP,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)));
                            } else {
                                break;
                            }
                        }
                    }

                }
                break;
            case ATTACK_BISHOP:
                final int BISHOP_ATTACK_LENGTH = 4;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    for (int i = 1; i <= BISHOP_ATTACK_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                if(gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)).getGameTeam() != gamePiece.getGameTeam()) {
                                    validActions.add(new AttackAction(gamePiece, ActionType.ATTACK_BISHOP, gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, i)), true));
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            case MOVEMENT_ROOK:
                final int ROOK_MOVEMENT_LENGTH = 3;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    for (int i = 1; i <= ROOK_MOVEMENT_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_ROOK,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)));
                            } else {
                                break;
                            }
                        }
                    }
                }
                break;
            case ATTACK_ROOK:
                final int ROOK_ATTACK_LENGTH = 3;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    for (int i = 1; i <= ROOK_ATTACK_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                if(gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)).getGameTeam() != gamePiece.getGameTeam()) {
                                    validActions.add(new AttackAction(gamePiece, ActionType.ATTACK_ROOK, gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, i)), true));
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            case MOVEMENT_QUEEN:
                final int QUEEN_MOVEMENT_LENGTH = 5;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    for (int i = 1; i <= QUEEN_MOVEMENT_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_ROOK,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)));
                            } else {
                                break;
                            }
                        }
                    }
                }
                break;
            case ATTACK_QUEEN:
                final int QUEEN_ATTACK_LENGTH = 3;
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    for (int i = 1; i <= QUEEN_ATTACK_LENGTH; i++) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                            if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                if(gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i)).getGameTeam() != gamePiece.getGameTeam()) {
                                    validActions.add(new AttackAction(gamePiece, ActionType.ATTACK_QUEEN, gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection, i)), true));
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            case ATTACK_KING:
                for(BoardDirection boardDirection: BoardDirection.values()) {
                    if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                        if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)) && gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)).getGameTeam() != gamePiece.getGameTeam()) {
                            validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_KING,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)), true));
                        }
                    }
                }
                break;
            case CASTLING:
                ArrayList<GamePiece> kingPieces = getGameBoard().getPiece(GamePieceType.KING,gamePiece.getGameTeam());
                if(kingPieces.size() > 0) {
                    ArrayList<GamePiece> validKingPieces = new ArrayList<>();
                    for(GamePiece unverifiedKingPiece: kingPieces) {
                        if(!hasPiecePreviouslyMoved(unverifiedKingPiece) && !hasPiecePreviouslyAttacked(unverifiedKingPiece)) {
                            validKingPieces.add(unverifiedKingPiece);
                        }
                    }
                    if(validKingPieces.size() > 0) {
                        ArrayList<GamePiece> rookPieces = getGameBoard().getPiece(GamePieceType.ROOK,gamePiece.getGameTeam());
                        if(rookPieces.size() > 0) {
                            ArrayList<GamePiece> validRookPieces = new ArrayList<>();
                            for (GamePiece unverifiedRookPiece : rookPieces) {
                                if (!hasPiecePreviouslyMoved(unverifiedRookPiece) && !hasPiecePreviouslyAttacked(unverifiedRookPiece)) {
                                    validRookPieces.add(unverifiedRookPiece);
                                }
                            }
                            if(validRookPieces.size() > 0) {
                                for(GamePiece validKing: validKingPieces) {
                                    for(GamePiece validRook: validRookPieces) {
                                        // We work out the direction
                                        BoardDirection rookDirection = validKing.getBoardLocation().getDirection(validRook.getBoardLocation());
                                        BoardLocation newKingPosition = validKing.getBoardLocation().getDirectionLocation(rookDirection,2);
                                        BoardLocation newRookPosition = newKingPosition.getDirectionLocation(rookDirection.reverse(),1);


                                        boolean terminate = false;
                                        for(int i = 1; i <= 3; i++) {
                                            if(!gameBoard.isSpaceOccupied(validKing.getBoardLocation().getDirectionLocation(rookDirection,i))) {

                                            } else {
                                                if(gameBoard.getPiece(validKing.getBoardLocation().getDirectionLocation(rookDirection,i)) == validRook) {

                                                } else {
                                                    terminate = true;
                                                }
                                            }
                                        }

                                        if(!terminate) {
                                            if (newKingPosition.matchesDirection(newRookPosition, rookDirection.reverse())) {
                                                validActions.add(new SpecialAction(validKing, ActionType.CASTLING, validKing.getBoardLocation(), newKingPosition, validRook, validRook.getBoardLocation(), newRookPosition));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;

        }
    }



    public boolean hasPiecePreviouslyAttackedThisTurn(GamePiece gamePiece) {
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

    public boolean hasPiecePreviouslyMovedThisTurn(GamePiece gamePiece) {
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

    public boolean hasPiecePreviouslyAttacked(GamePiece gamePiece) {
        if(pastActions.size() != 0) {
            for (Action action : pastActions) {
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
        if(pastActions.size() != 0) {
            for (Action action : pastActions) {
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
        if(currentTeamTurn == GameTeam.BLACK) {
            if(isUseAIMode()) {
                aiController.onTurnStartPhase(this);
            }
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
        return new GameState(this,branchingAction,aiController);
    }

    @Override
    public String toString() {
        return "GameState: Turn: " + currentTurnNumber + "\n" + gameBoard;

    }

    public boolean isUseAIMode() {
        return useAIMode;
    }

    public void toggleAIMode(AIController aiController) {
        this.useAIMode = !useAIMode;
        if(useAIMode) {
            if (currentTeamTurn == GameTeam.BLACK) {
                aiController.onTurnStartPhase(this);

            }
        }
        // Check if using AI mode
    }
}
