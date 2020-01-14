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
        for(ActionType actionType: availableActions) {
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
                    if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getOffsetLocation(0,offset))) {
                        if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getOffsetLocation(0,offset))) {
                            if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getOffsetLocation(0,offset * 2))) {
                                if(!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getOffsetLocation(0,offset * 2))) {
                                    validActions.add(new MovementAction(gamePiece,ActionType.MOVEMENT_PAWN_ADVANCE,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getOffsetLocation(0,offset * 2)));
                                }
                            }
                        }
                    }
                    break;
                case ATTACK_PAWN_EN_PASSANT:
                    // TODO See if teacher is requring it, if so check the Turn Log for last turn, see if a pawn advanced, if the current piece is in the right now
                    // If the pawn in question is in a valid position
                case ATTACK_PAWN:
                    switch(gamePiece.getGameTeam()) {
                        case WHITE:
                            if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.NORTH_EAST,1))) {
                                if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.NORTH_EAST,1))) {
                                    validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_PAWN,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.NORTH_EAST,1)));
                                }
                            }
                            if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.NORTH_WEST,1))) {
                                if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.NORTH_WEST,1))) {
                                    validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_PAWN,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.NORTH_WEST,1)));
                                }
                            }
                            break;
                        case BLACK:
                            if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.SOUTH_EAST,1))) {
                                if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.SOUTH_EAST,1))) {
                                    validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_PAWN,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.SOUTH_EAST,1)));
                                }
                            }
                            if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.SOUTH_WEST,1))) {
                                if (!gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.SOUTH_WEST,1))) {
                                    validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_PAWN,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(BoardDirection.SOUTH_WEST,1)));
                                }
                            }
                            break;
                    }
                    break;
                case MOVEMENT_KNIGHT:
                    final BoardLocation[][] knightMovements = new BoardLocation[][]{
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,1),gamePiece.getBoardLocation().getOffsetLocation(0,2),gamePiece.getBoardLocation().getOffsetLocation(0,3),gamePiece.getBoardLocation().getOffsetLocation(1,3)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,1),gamePiece.getBoardLocation().getOffsetLocation(0,2),gamePiece.getBoardLocation().getOffsetLocation(0,3),gamePiece.getBoardLocation().getOffsetLocation(-1,3)},

                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,-1),gamePiece.getBoardLocation().getOffsetLocation(0,-2),gamePiece.getBoardLocation().getOffsetLocation(0,-3),gamePiece.getBoardLocation().getOffsetLocation(1,-3)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,-1),gamePiece.getBoardLocation().getOffsetLocation(0,-2),gamePiece.getBoardLocation().getOffsetLocation(0,-3),gamePiece.getBoardLocation().getOffsetLocation(-1,-3)},

                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(1,0),gamePiece.getBoardLocation().getOffsetLocation(2,0),gamePiece.getBoardLocation().getOffsetLocation(3,0),gamePiece.getBoardLocation().getOffsetLocation(3,1)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(1,0),gamePiece.getBoardLocation().getOffsetLocation(2,0),gamePiece.getBoardLocation().getOffsetLocation(3,0),gamePiece.getBoardLocation().getOffsetLocation(3,-1)},

                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(-1,0),gamePiece.getBoardLocation().getOffsetLocation(-2,0),gamePiece.getBoardLocation().getOffsetLocation(-3,0),gamePiece.getBoardLocation().getOffsetLocation(-3,1)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(-1,0),gamePiece.getBoardLocation().getOffsetLocation(-2,0),gamePiece.getBoardLocation().getOffsetLocation(-3,0),gamePiece.getBoardLocation().getOffsetLocation(-3,-1)},
                    };


                    for(int i = 0; i < knightMovements.length; i++) {
                        if(gameBoard.isInsideBoard(knightMovements[i][3])) {
                            if (!gameBoard.isSpaceOccupied(knightMovements[i][3])) {
                                validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_PAWN,gamePiece.getBoardLocation(),knightMovements[i][3]));
                            }
                        }
                    }
                    break;
                case ATTACK_KNIGHT:
                    final BoardLocation[][] knightAttacks = new BoardLocation[][]{
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,1),gamePiece.getBoardLocation().getOffsetLocation(0,2),gamePiece.getBoardLocation().getOffsetLocation(0,3),gamePiece.getBoardLocation().getOffsetLocation(1,3)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,1),gamePiece.getBoardLocation().getOffsetLocation(0,2),gamePiece.getBoardLocation().getOffsetLocation(0,3),gamePiece.getBoardLocation().getOffsetLocation(-1,3)},

                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,-1),gamePiece.getBoardLocation().getOffsetLocation(0,-2),gamePiece.getBoardLocation().getOffsetLocation(0,-3),gamePiece.getBoardLocation().getOffsetLocation(1,-3)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(0,-1),gamePiece.getBoardLocation().getOffsetLocation(0,-2),gamePiece.getBoardLocation().getOffsetLocation(0,-3),gamePiece.getBoardLocation().getOffsetLocation(-1,-3)},

                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(1,0),gamePiece.getBoardLocation().getOffsetLocation(2,0),gamePiece.getBoardLocation().getOffsetLocation(3,0),gamePiece.getBoardLocation().getOffsetLocation(3,1)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(1,0),gamePiece.getBoardLocation().getOffsetLocation(2,0),gamePiece.getBoardLocation().getOffsetLocation(3,0),gamePiece.getBoardLocation().getOffsetLocation(3,-1)},

                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(-1,0),gamePiece.getBoardLocation().getOffsetLocation(-2,0),gamePiece.getBoardLocation().getOffsetLocation(-3,0),gamePiece.getBoardLocation().getOffsetLocation(-3,1)},
                            new BoardLocation[]{gamePiece.getBoardLocation().getOffsetLocation(-1,0),gamePiece.getBoardLocation().getOffsetLocation(-2,0),gamePiece.getBoardLocation().getOffsetLocation(-3,0),gamePiece.getBoardLocation().getOffsetLocation(-3,-1)},
                    };


                    for(int i = 0; i < knightAttacks.length; i++) {
                        if(gameBoard.isInsideBoard(knightAttacks[i][3])) {
                            if (gameBoard.isSpaceOccupied(knightAttacks[i][3])) {
                                validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_PAWN,gamePiece.getBoardLocation(),knightAttacks[i][3]));
                            }
                        }
                    }                    break;
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
                                    if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                        passCount++;
                                    }
                                }
                            }
                        }
                        if(passCount == BISHOP_ATTACK_LENGTH) {
                            validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_BISHOP,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,BISHOP_ATTACK_LENGTH)));
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
                                    if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                        passCount++;
                                    }
                                }
                            }
                        }
                        if(passCount == ROOK_ATTACK_LENGTH) {
                            validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_ROOK,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,ROOK_ATTACK_LENGTH)));
                        }
                    }
                    break;
                case MOVEMENT_QUEEN:
                    final int QUEEN_MOVEMENT_LENGTH = 3;
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
                                    if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,i))) {
                                        passCount++;
                                    }
                                }
                            }
                        }
                        if(passCount == QUEEN_ATTACK_LENGTH) {
                            validActions.add(new MovementAction(gamePiece,ActionType.ATTACK_ROOK,gamePiece.getBoardLocation(),gamePiece.getBoardLocation().getDirectionLocation(boardDirection,QUEEN_ATTACK_LENGTH)));
                        }
                    }
                    break;
                case MOVEMENT_CASTLE_KING:
                    // Todo Add Castling Logic
                    break;
                case ATTACK_KING:
                    for(BoardDirection boardDirection: BoardDirection.values()) {
                        if(gameBoard.isInsideBoard(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                            if(gameBoard.isSpaceOccupied(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1))) {
                                validActions.add(new AttackAction(gamePiece,ActionType.ATTACK_KING,gameBoard.getPiece(gamePiece.getBoardLocation().getDirectionLocation(boardDirection,1)), false));
                            }
                        }
                    }
                    break;
            }
        }
        return validActions;
    }

    public void preformAction(Action action) {
        action.preformAction(gameBoard);
        pastActions.add(action);
        if(pastActions.size() % ACTIONS_PER_TURN == 0) {
            switchTurn();
        }
    }

    public void switchTurn() {
        currentTeamTurn = getNextTeamTurn();
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
}
