package GameComponents.Controllers;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;
import GameComponents.Board.Turn.*;
import GameComponents.GUIRenderer;
import GameComponents.GameState;

import java.util.ArrayList;
import java.util.HashMap;

// Works with Player Input from the Game window and allows for players to modify the game
public class PlayerController {
    private GameState gameState;
    private final GUIRenderer guiRenderer;

    private BoardLocation boardLocation;
    private HashMap<BoardLocation,Action> actionMoveSet;
    public PlayerController(GameState gameState, GUIRenderer guiRenderer) {
        this.gameState = gameState;
        this.guiRenderer = guiRenderer;
        this.boardLocation = null;
        this.actionMoveSet = new HashMap<>();
    }

    // Looks at where the player left clicks
    public void checkMouseLeftClick(int canvasX, int canvasY) {
        if(!gameState.isUseAIMode() || gameState.getCurrentTeamTurn() == GameTeam.WHITE) {
            // Check if its actually inside the damn thing
            if (canvasX >= GUIRenderer.BOARD_X_OFFSET && canvasY >= GUIRenderer.BOARD_Y_OFFSET && canvasX <= GUIRenderer.BOARD_X_OFFSET + GUIRenderer.BOARD_LENGTH && canvasY <= GUIRenderer.BOARD_Y_OFFSET + GUIRenderer.BOARD_LENGTH) {
                int squareX = (canvasX - GUIRenderer.BOARD_X_OFFSET) / GUIRenderer.PIECE_LENGTH;
                int squareY = (canvasY - GUIRenderer.BOARD_Y_OFFSET) / GUIRenderer.PIECE_LENGTH;

                if (boardLocation == null) {
                    if (gameState.getGameBoard().isInsideBoard(squareX, squareY) && gameState.getGameBoard().isSpaceOccupied(squareX, squareY)) {
                        GamePiece selectedPiece = gameState.getGameBoard().getPiece(squareX, squareY);
                        this.boardLocation = selectedPiece.getBoardLocation();

                        guiRenderer.getSelectedPieces().clear();
                        guiRenderer.getSelectedMoveAreas().clear();
                        guiRenderer.getSelectedAttackAreas().clear();
                        guiRenderer.clearAttackChances();
                        guiRenderer.getSelectedPieces().add(selectedPiece.getBoardLocation());

                        ArrayList<Action> possibleActions = gameState.getValidActions(selectedPiece);
                        if (possibleActions.size() > 0) {
                            for (Action action : possibleActions) {
                                if (action instanceof MovementAction) {
                                    guiRenderer.getSelectedMoveAreas().add(((MovementAction) action).getNewLocation());
                                    actionMoveSet.put(((MovementAction) action).getNewLocation(), action);
                                    if(action instanceof SpecialAction) {
                                        guiRenderer.getSelectedPieces().add(((SpecialAction) action).getSecondaryPiece().getBoardLocation());
                                    }
                                }
                                if (action instanceof AttackAction) {
                                    guiRenderer.getSelectedAttackAreas().add(((AttackAction) action).getNewLocation());
                                    actionMoveSet.put(((AttackAction) action).getNewLocation(), action);
                                    guiRenderer.getAttackChances()[((AttackAction) action).getNewLocation().getX()][((AttackAction) action).getNewLocation().getY()] = (int) (action.getGamePiece().getGamePieceType().requiredRoll(((AttackAction) action).getTargetPiece().getGamePieceType()));
                                }
                            }
                        }
                    } else {
                        guiRenderer.getSelectedPieces().clear();
                        guiRenderer.getSelectedMoveAreas().clear();
                        guiRenderer.getSelectedAttackAreas().clear();
                        boardLocation = null;
                        actionMoveSet.clear();
                    }
                } else {
                    if (actionMoveSet.size() > 0) {
                        for (BoardLocation boardLocation : actionMoveSet.keySet()) {
                            if (boardLocation.getX() == squareX && boardLocation.getY() == squareY) {
                                gameState.preformAction(actionMoveSet.get(boardLocation));
                                break;
                            }
                        }
                    }
                    guiRenderer.getSelectedPieces().clear();
                    guiRenderer.getSelectedMoveAreas().clear();
                    guiRenderer.getSelectedAttackAreas().clear();
                    boardLocation = null;
                    actionMoveSet.clear();
                }
            }
        }
    }

    // Looks at where players right click
    public void checkMouseRightClick(int canvasX, int canvasY) {

        // Check if its actually inside the damn thing
        gameState.getGameBoard().clear();
        gameState.getGameBoard().addPiece(new GamePiece(GameTeam.BLACK, GamePieceType.QUEEN, new BoardLocation(1,1)), new BoardLocation(1,1));
        gameState.getGameBoard().addPiece(new GamePiece(GameTeam.WHITE, GamePieceType.KING, new BoardLocation(0,0)), new BoardLocation(0,0));
        gameState.getGameBoard().addPiece(new GamePiece(GameTeam.BLACK, GamePieceType.KING, new BoardLocation(3,3)), new BoardLocation(3,3));
        gameState.toggleAIMode(gameState.getAiController());
        gameState.preformAction(new NothingAction(null));
        gameState.preformAction(new NothingAction(null));

    }


    public void checkMouseMovement(double canvasX, double canvasY) {

        double adjustedX = canvasX - GUIRenderer.BOARD_X_OFFSET - GUIRenderer.BOARD_SQUARE_OFFSET;
        double adjustedY = canvasY - GUIRenderer.BOARD_Y_OFFSET - GUIRenderer.BOARD_SQUARE_OFFSET;
        int locX = (int) (adjustedX / GUIRenderer.PIECE_LENGTH);
        int locY = (int) (adjustedY / GUIRenderer.PIECE_LENGTH);

        if(adjustedX >=0 && adjustedY >= 0 && adjustedX <= gameState.getGameBoard().getBOARD_WIDTH()*GUIRenderer.PIECE_LENGTH && adjustedY <= gameState.getGameBoard().getBOARD_HEIGHT()*GUIRenderer.PIECE_LENGTH ) {
            if (locX >= 0 && locY >= 0 && locX < gameState.getGameBoard().getBOARD_WIDTH() && locY < gameState.getGameBoard().getBOARD_HEIGHT()) {
                guiRenderer.setCurrentCursorPosition(new BoardLocation(locX, locY));
            } else {
                guiRenderer.setCurrentCursorPosition(null);
            }
        } else {
            guiRenderer.setCurrentCursorPosition(null);
        }
    }


    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
