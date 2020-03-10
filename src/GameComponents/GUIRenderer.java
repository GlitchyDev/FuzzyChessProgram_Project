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
    private final int DEBUG_TEXTOFFSET = 300;
    private final Color DEBUG_COLOR = Color.LIME;
    private final int DEBUG_CORDOFFSET = 10;

    // Selected Pieces
    private final ArrayList<BoardLocation> selectedPieces;
    private final ArrayList<BoardLocation> selectedMoveAreas;
    private final ArrayList<BoardLocation> selectedAttackAreas;
    private final Color SELECTED_PIECE_COLOR = Color.AQUA;
    private final Color SELECTED_MOVE_COLOR = Color.GREEN;
    private final Color SELECTED_ATTACK_COLOR = Color.RED;
    private final double SELECTED_OPACITY = 0.5;



    private GameState gameState;
    private final double canvasWidth;
    private final double canvasHeight;
    private int debug = 0;

    private final String pieceFolder = "100";


    public GUIRenderer(GameState gameState, double canvasWidth, double canvasHeight) {
        this.gameState = gameState;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        selectedPieces = new ArrayList<>();
        selectedMoveAreas = new ArrayList<>();
        selectedAttackAreas = new ArrayList<>();
    }


    // Renders the GUI of the program, does it once every frame
    public void renderGUI(GraphicsContext gc) {
        // Clear Field
        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(1.0);
        gc.fillRect(0,0,canvasWidth,canvasHeight);


        renderBoard(gc);

        debug++;

        // Renders all the Selected Piece Boxes
        gc.setGlobalAlpha(SELECTED_OPACITY);
        gc.setFill(SELECTED_PIECE_COLOR);
        if(selectedPieces.size() > 0) {
            for(BoardLocation boardLocation: selectedPieces) {
                gc.fillOval(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);

            }
        }
        // Renders all the Movement Option Boxes
        gc.setFill(SELECTED_MOVE_COLOR);
        if(selectedMoveAreas.size() > 0) {
            for(BoardLocation boardLocation: selectedMoveAreas) {
                gc.fillRect(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);

            }
        }
        // Renders all the Attack option Boxes
        gc.setFill(SELECTED_ATTACK_COLOR);
        if(selectedAttackAreas.size() > 0) {
            for(BoardLocation boardLocation: selectedAttackAreas) {
                gc.fillRect(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + boardLocation.getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);

            }
        }
        gc.setGlobalAlpha(1.0);



        debugString = "";
        // Renders the Debug String
        gc.setFill(DEBUG_COLOR);
        gc.fillText(debugString,0,DEBUG_TEXTOFFSET);

        if(gameState.isUseAIMode()) {
            gc.setFill(DEBUG_COLOR);
            gc.fillText("Using AI Mode",0,10);
        }

    }

    // Renders the physical game board
    public void renderBoard(GraphicsContext gc) {
        gc.setFill(BOARD_COLOR);
        gc.fillRect(BOARD_X_OFFSET,BOARD_Y_OFFSET,BOARD_LENGTH,BOARD_LENGTH);


        // Renders the Debug square to keep the lil guy alive
        gc.setFill(Color.BLUE);
        gc.fillRect(debug%canvasWidth,debug/canvasWidth%canvasHeight,10,10);

        boolean isBlack = false;
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                gc.setFill(isBlack ? SQUARE_WHITE_COLOR : SQUARE_BLACK_COLOR);
                gc.fillRect(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + x * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + y * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
                isBlack = !isBlack;
            }
            isBlack = !isBlack;
        }
        renderDebugCords(gc);

        renderGamePieces(gc);
    }

    // Renders the game pieces
    private void renderGamePieces(GraphicsContext gc) {
        for(GamePiece gamePiece: gameState.getGameBoard().getBlackPieces()) {
            renderPiece(gc,gamePiece);
        }
        for(GamePiece gamePiece: gameState.getGameBoard().getWhitePieces()) {
            renderPiece(gc,gamePiece);
        }
    }

    // Renders an individual piece with its specified image from the pieceFolder
    private void renderPiece(GraphicsContext gc, GamePiece gamePiece) {
        gc.setFill(gamePiece.getGameTeam() == GameTeam.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);

        switch(gamePiece.getGameTeam()) {
            case BLACK:
                switch(gamePiece.getGamePieceType()) {
                    case PAWN:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/BP.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case KNIGHT:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/BN.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case BISHOP:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/BB.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case ROOK:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/BR.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case QUEEN:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/BQ.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case KING:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/BK.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                }
                break;
            case WHITE:
                switch(gamePiece.getGamePieceType()) {
                    case PAWN:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/WP.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case KNIGHT:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/WN.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case BISHOP:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/WB.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case ROOK:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/WR.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case QUEEN:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/WQ.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                    case KING:
                        gc.drawImage(FileLoader.getImage(pieceFolder+ "/WK.png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
                        break;
                }
                break;
        }
        //gc.fillOval(BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
        //gc.setFill(TEXT_COLOR);
        //gc.fillText(gamePiece.getGamePieceType().toString(),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH + PIECE_TEXTOFFSET);
    }

    // Renders all the game board coordinates ( Replace with a proper board location stuff once we can )
    private void renderDebugCords(GraphicsContext gc) {
        gc.setFill(DEBUG_COLOR);
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                gc.fillText(x + "," + y,BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + x * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + y * PIECE_LENGTH + DEBUG_CORDOFFSET);
            }
        }
    }

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }

    public String getDebugString() {
        return debugString;
    }

    public ArrayList<BoardLocation> getSelectedMoveAreas() {
        return selectedMoveAreas;
    }

    public ArrayList<BoardLocation> getSelectedAttackAreas() {
        return selectedAttackAreas;
    }

    public ArrayList<BoardLocation> getSelectedPieces() {
        return selectedPieces;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
