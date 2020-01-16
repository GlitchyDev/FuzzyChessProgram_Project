package GameComponents;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * This class specifically handles all GUI rendering for a Game Window. In the future this class will need to account
 * for window resizing to properly render to new size
 */
public class GUIRenderer {
    // BOARD
    public static final int BOARD_LENGTH = 420;
    private final Color BOARD_COLOR = Color.RED;
    public static final int BOARD_Y_OFFSET = 0;
    public static final int BOARD_X_OFFSET = 0;

    // SQUARES
    public static final int BOARD_SQUARE_OFFSET = 10;
    private final Color SQUARE_WHITE_COLOR = Color.LIGHTGRAY;
    private final Color SQUARE_BLACK_COLOR = Color.DARKGRAY;

    // PIECES
    public static final int PIECE_LENGTH = 50;
    public static final int PIECE_TEXTOFFSET = 25;
    private final Color WHITE_PIECE_COLOR = Color.WHITE;
    private final Color BLACK_PIECE_COLOR = Color.BLACK;
    private final Color TEXT_COLOR = Color.BLUE;

    // DEBUG
    private String debugString = "";
    private final int DEBUG_TEXTOFFSET = 440;
    private final Color DEBUG_COLOR = Color.LIME;

    // Selected Pieces
    private final ArrayList<BoardLocation> selectedPieces;
    private final ArrayList<BoardLocation> selectedAreas;
    private final Color SELECTED_PIECE_COLOR = Color.AQUA;
    private final Color SELECTED_AREA_COLOR = Color.GREEN;
    private final double SELECTED_OPACITY = 0.5;



    private final GameState gameState;
    private final double canvasWidth;
    private final double canvasHeight;
    private int debug = 0;



    public GUIRenderer(GameState gameState, double canvasWidth, double canvasHeight) {
        this.gameState = gameState;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        selectedPieces = new ArrayList<>();
        selectedAreas = new ArrayList<>();
    }



    public void renderGUI(GraphicsContext gc) {
        // Clear Field
        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(1.0);
        gc.fillRect(0,0,canvasWidth,canvasHeight);

        renderBoard(gc);

        debug++;

        gc.setFill(Color.BLUE);
        gc.fillRect(debug%canvasWidth,debug/canvasWidth%canvasHeight,10,10);

        gc.setGlobalAlpha(SELECTED_OPACITY);
        gc.setFill(SELECTED_PIECE_COLOR);
        if(selectedPieces.size() > 0) {
            for(BoardLocation boardLocation: selectedPieces) {
                gc.fillOval(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);

            }
        }
        if(selectedAreas.size() > 0) {
            for(BoardLocation boardLocation: selectedAreas) {
                gc.fillRect(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);

            }
        }
        gc.setGlobalAlpha(1.0);


        gc.setFill(DEBUG_COLOR);
        gc.fillText(debugString,0,DEBUG_TEXTOFFSET);
    }

    public void renderBoard(GraphicsContext gc) {
        gc.setFill(BOARD_COLOR);
        gc.fillRect(BOARD_X_OFFSET,BOARD_Y_OFFSET,BOARD_LENGTH,BOARD_LENGTH);

        boolean isBlack = false;
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                gc.setFill(isBlack ? SQUARE_WHITE_COLOR : SQUARE_BLACK_COLOR);
                gc.fillRect(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + x * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + y * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
                isBlack = !isBlack;
            }
            isBlack = !isBlack;
        }

        debugRenderPieces(gc);
    }

    private void debugRenderPieces(GraphicsContext gc) {
        for(GamePiece gamePiece: gameState.getGameBoard().getBlackPieces()) {
            renderPiece(gc,gamePiece);
        }
        for(GamePiece gamePiece: gameState.getGameBoard().getWhitePieces()) {
            renderPiece(gc,gamePiece);
        }
    }

    private void renderPiece(GraphicsContext gc, GamePiece gamePiece) {
        gc.setFill(gamePiece.getGameTeam() == GameTeam.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);
        gc.fillOval(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
        gc.setFill(TEXT_COLOR);
        gc.fillText(gamePiece.getGamePieceType().toString(),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH + PIECE_TEXTOFFSET);
    }

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }

    public String getDebugString() {
        return debugString;
    }

    public ArrayList<BoardLocation> getSelectedAreas() {
        return selectedAreas;
    }

    public ArrayList<BoardLocation> getSelectedPieces() {
        return selectedPieces;
    }
}