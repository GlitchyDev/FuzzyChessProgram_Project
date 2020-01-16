package GameComponents.Controllers;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.Board.Turn.AttackAction;
import GameComponents.Board.Turn.MovementAction;
import GameComponents.GUIRenderer;
import GameComponents.GameState;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerController {
    private final GameState gameState;
    private final GUIRenderer guiRenderer;

    private BoardLocation boardLocation;
    private HashMap<BoardLocation,Action> actionMoveSet;
    public PlayerController(GameState gameState, GUIRenderer guiRenderer) {
        this.gameState = gameState;
        this.guiRenderer = guiRenderer;
        this.boardLocation = null;
        this.actionMoveSet = new HashMap<>();
    }

    public void checkMouseLeftClick(int canvasX, int canvasY) {
        // Check if its actually inside the damn thing
        if(canvasX >= GUIRenderer.BOARD_X_OFFSET && canvasY >= GUIRenderer.BOARD_Y_OFFSET && canvasX <= GUIRenderer.BOARD_X_OFFSET + GUIRenderer.BOARD_LENGTH && canvasY <= GUIRenderer.BOARD_Y_OFFSET + GUIRenderer.BOARD_LENGTH) {
            int squareX = (canvasX-GUIRenderer.BOARD_X_OFFSET)/GUIRenderer.PIECE_LENGTH;
            int squareY = (canvasY-GUIRenderer.BOARD_Y_OFFSET)/GUIRenderer.PIECE_LENGTH;

            if(boardLocation == null) {
                if (gameState.getGameBoard().isSpaceOccupied(squareX, squareY)) {
                    GamePiece selectedPiece = gameState.getGameBoard().getPiece(squareX, squareY);
                    this.boardLocation = selectedPiece.getBoardLocation();

                    guiRenderer.getSelectedPieces().clear();
                    guiRenderer.getSelectedMoveAreas().clear();
                    guiRenderer.getSelectedAttackAreas().clear();
                    guiRenderer.getSelectedPieces().add(selectedPiece.getBoardLocation());

                    ArrayList<Action> possibleActions = gameState.getValidActions(selectedPiece);
                    if (possibleActions.size() > 0) {
                        for (Action action : possibleActions) {
                            if (action instanceof MovementAction) {
                                guiRenderer.getSelectedMoveAreas().add(((MovementAction) action).getNewLocation());
                                actionMoveSet.put(((MovementAction) action).getNewLocation(),action);
                            }
                            if (action instanceof AttackAction) {
                                guiRenderer.getSelectedAttackAreas().add(((AttackAction) action).getNewLocation());
                                actionMoveSet.put(((AttackAction) action).getNewLocation(),action);
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
                if(actionMoveSet.size() > 0) {
                    for(BoardLocation boardLocation: actionMoveSet.keySet()) {
                        if(boardLocation.getX() == squareX && boardLocation.getY() == squareY) {
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


    public void checkMouseRightClick(int canvasX, int canvasY) {
        // Check if its actually inside the damn thing
        if(canvasX >= GUIRenderer.BOARD_X_OFFSET && canvasY >= GUIRenderer.BOARD_Y_OFFSET && canvasX <= GUIRenderer.BOARD_X_OFFSET + GUIRenderer.BOARD_LENGTH && canvasY <= GUIRenderer.BOARD_Y_OFFSET + GUIRenderer.BOARD_LENGTH) {
            int squareX = (canvasX - GUIRenderer.BOARD_X_OFFSET) / GUIRenderer.PIECE_LENGTH;
            int squareY = (canvasY - GUIRenderer.BOARD_Y_OFFSET) / GUIRenderer.PIECE_LENGTH;


            if (gameState.getGameBoard().isSpaceOccupied(squareX, squareY)) {
                GamePiece gamePiece = gameState.getGameBoard().getPiece(squareX, squareY);

                ArrayList<Action> actions = gameState.getValidActions(gamePiece);
                String debugString = "";
                for(Action action: actions) {
                    debugString += action.toString() + "\n";
                }
                guiRenderer.setDebugString(debugString);
            }  else {
                guiRenderer.setDebugString("");
            }
        }
    }
}
