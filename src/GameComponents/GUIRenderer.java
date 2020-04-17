package GameComponents;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;
import GameComponents.Board.Turn.Action;
import GameComponents.Board.Turn.AttackAction;
import GameComponents.Board.Turn.MovementAction;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class specifically handles all GUI rendering for a Game Window. In the future this class will need to account
 * for window resizing to properly render to new size
 */
public class GUIRenderer {
    private ProgramWindow gameWindow;
    // BOARD
    private final static int WINDOW_LENGTH = 420;
    public static int WINDOW_WIDTH = WINDOW_LENGTH-10;
    public static int WINDOW_HEIGHT = WINDOW_LENGTH+15+100;

    public static final int BOARD_LENGTH = 420;
    private final Color BOARD_COLOR = Color.RED;
    public static final int BOARD_Y_OFFSET = 100;
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
    private int[][] attackChances;
    private final Color SELECTED_PIECE_COLOR = Color.AQUA;
    private final Color SELECTED_MOVE_COLOR = Color.GREEN;
    private final Color SELECTED_ATTACK_COLOR = Color.RED;
    private final double SELECTED_OPACITY = 0.5;

    private BoardLocation currentCursorPosition;



    private GameState gameState;
    private double canvasWidth;
    private double canvasHeight;
    private int debug = 0;

    private final String pieceFolder = "/Pieces";
    private final String otherFolder = "/OtherSprites";


    public GUIRenderer(GameState gameState, ProgramWindow gameWindow, double canvasWidth, double canvasHeight) {
        this.gameState = gameState;
        this.gameWindow = gameWindow;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        selectedPieces = new ArrayList<>();
        selectedMoveAreas = new ArrayList<>();
        selectedAttackAreas = new ArrayList<>();
        attackChances = new int[gameState.getGameBoard().getBOARD_WIDTH()][gameState.getGameBoard().getBOARD_HEIGHT()];
        currentCursorPosition = null;
    }


    // Renders the GUI of the program, does it once every frame
    public void renderGUI(GraphicsContext gc) {
        // Clear Field
        gc.setFill(Color.WHITE);
        gc.setGlobalAlpha(1.0);
        gc.fillRect(0,0,canvasWidth*2,canvasHeight);


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
            if(gameWindow.isUseAIDebugMode()) {
                gc.fillText("Using AI Debug Mode", 0, 20);
                renderAIDebugMode(gc);
            }
        }

        renderInfoBoard(gc);

        if(gameState.getCurrentTeamTurn() == GameTeam.BLACK_WIN) {
            gc.drawImage(FileLoader.getImage("OtherSprites/BWin.png"),WINDOW_WIDTH/2-100,WINDOW_HEIGHT/2-100);
        }
        if(gameState.getCurrentTeamTurn() == GameTeam.WHITE_WIN) {
            gc.drawImage(FileLoader.getImage("OtherSprites/WWin.png"),WINDOW_WIDTH/2-100,WINDOW_HEIGHT/2-100);
        }

    }

    public void renderInfoBoard(GraphicsContext gc) {
        gc.drawImage(FileLoader.getImage("/GameBoard/" + "Boardtop" + ".png"),0,0);
        ArrayList<Action> currentActions = gameState.getTurnActions(gameState.getCurrentTurnNumber());
        for(int i = 0; i < currentActions.size(); i++) {
            Action action = currentActions.get(i);
            if(action instanceof AttackAction) {
                gc.drawImage(getPieceImage(action.getGamePiece()),10+PIECE_LENGTH * i,0,PIECE_LENGTH, PIECE_LENGTH);
                gc.setFill(Color.BLUE);
                gc.fillText(action.getGamePiece().getBoardLocation().toFormal(), 10+PIECE_LENGTH * i, 10);
                String attackResult = otherFolder + "/" + (action.getGamePiece().getGameTeam() == GameTeam.WHITE ? "W" : "B") + "Attack" + (((AttackAction) action).isSuccessful() ? "Win" : "Lose") + ".png";
                gc.drawImage(FileLoader.getImage(attackResult), 10+PIECE_LENGTH * i, PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
            }
            if(action instanceof MovementAction) {
                gc.drawImage(getPieceImage(action.getGamePiece()),10+PIECE_LENGTH * i,0,PIECE_LENGTH, PIECE_LENGTH);
                gc.fillText(action.getGamePiece().getBoardLocation().toFormal(),PIECE_LENGTH * i,10);
                gc.drawImage(FileLoader.getImage(otherFolder + "/" + "Move" + ".png"), 10+PIECE_LENGTH * i,PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
            }
        }

        if(gameState.getCurrentTurnNumber() > 1) {
            ArrayList<Action> pastActions = gameState.getTurnActions(gameState.getCurrentTurnNumber()-1);
            for (int i = 0; i < pastActions.size(); i++) {
                Action action = pastActions.get(i);
                if (action instanceof AttackAction) {
                    gc.drawImage(getPieceImage(action.getGamePiece()), ((WINDOW_WIDTH-PIECE_LENGTH*2)) + PIECE_LENGTH * i, 0, PIECE_LENGTH, PIECE_LENGTH);
                    gc.setFill(Color.BLUE);
                    gc.fillText(action.getGamePiece().getBoardLocation().toString(), ((WINDOW_WIDTH-PIECE_LENGTH*2)) + PIECE_LENGTH * i, 10);
                    String attackResult = otherFolder + "/" + (action.getGamePiece().getGameTeam() == GameTeam.WHITE ? "W" : "B") + "Attack" + (((AttackAction) action).isSuccessful() ? "Win" : "Lose") + ".png";
                    gc.drawImage(FileLoader.getImage(attackResult), ((WINDOW_WIDTH-PIECE_LENGTH*2)) + PIECE_LENGTH * i, PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
                }
                if (action instanceof MovementAction) {
                    gc.drawImage(getPieceImage(action.getGamePiece()), ((WINDOW_WIDTH-PIECE_LENGTH*2)) + PIECE_LENGTH * i, 0, PIECE_LENGTH, PIECE_LENGTH);
                    gc.fillText(action.getGamePiece().getBoardLocation().toFormal(), ((WINDOW_WIDTH-PIECE_LENGTH*2)) + PIECE_LENGTH * i, 10);
                    gc.drawImage(FileLoader.getImage(otherFolder + "/" + "Move" + ".png"), ((WINDOW_WIDTH-PIECE_LENGTH*2)) + PIECE_LENGTH * i, PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
                }
            }

        }

        if(currentCursorPosition != null && (gameState.getCurrentTeamTurn() == GameTeam.WHITE || !gameState.isUseAIMode())) {
            renderAttackHelp(gc);
        }
    }

    // Renders the physical game board
    public void renderBoard(GraphicsContext gc) {
        gc.drawImage(FileLoader.getImage("/GameBoard/BorderNoExtension.png"),BOARD_X_OFFSET,BOARD_Y_OFFSET,BOARD_LENGTH,BOARD_LENGTH);



        // Renders the Debug square to keep the lil guy alive
        gc.setFill(Color.BLUE);
        gc.fillRect(debug%canvasWidth,debug/canvasWidth%canvasHeight,10,10);

        boolean isBlack = false;
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                if(isBlack) {
                    gc.drawImage(FileLoader.getImage("/GameBoard/BTile2.png"), BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + x * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + y * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);
                } else {
                    gc.drawImage(FileLoader.getImage("/GameBoard/WTile2.png"), BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + x * PIECE_LENGTH, BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + y * PIECE_LENGTH, PIECE_LENGTH, PIECE_LENGTH);

                }
                isBlack = !isBlack;

            }
            isBlack = !isBlack;
        }
        //renderDebugCords(gc);

        renderGamePieces(gc);
    }

    private ArrayList<String> aiDebugInformation = new ArrayList<>(Arrays.asList("","",""));

    private void renderAIDebugMode(GraphicsContext gc) {
        for(int i = 0; i < aiDebugInformation.size(); i++) {
            gc.fillText(aiDebugInformation.get(i),canvasWidth,i * 10);
        }

    }

    public void recordAction1Text(String string) {
        aiDebugInformation.set(1,string);

    }

    public void recordAction2Text(String string) {
        aiDebugInformation.set(2,string);
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

    public Image getPieceImage(GamePiece gamePiece) {
        switch(gamePiece.getGameTeam()) {
            case BLACK:
                switch(gamePiece.getGamePieceType()) {
                    case PAWN:
                        return FileLoader.getImage(pieceFolder+ "/BP.png");
                    case KNIGHT:
                        return FileLoader.getImage(pieceFolder+ "/BN.png");
                    case BISHOP:
                        return FileLoader.getImage(pieceFolder+ "/BB.png");
                    case ROOK:
                        return FileLoader.getImage(pieceFolder+ "/BR.png");
                    case QUEEN:
                        return FileLoader.getImage(pieceFolder+ "/BQ.png");
                    case KING:
                        return FileLoader.getImage(pieceFolder+ "/BK.png");
                }
                break;
            case WHITE:
                switch(gamePiece.getGamePieceType()) {
                    case PAWN:
                        return FileLoader.getImage(pieceFolder+ "/WP.png");
                    case KNIGHT:
                        return FileLoader.getImage(pieceFolder+ "/WN.png");
                    case BISHOP:
                        return FileLoader.getImage(pieceFolder+ "/WB.png");
                    case ROOK:
                        return FileLoader.getImage(pieceFolder+ "/WR.png");
                    case QUEEN:
                        return FileLoader.getImage(pieceFolder+ "/WQ.png");
                    case KING:
                        return FileLoader.getImage(pieceFolder+ "/WK.png");
                }
                break;
        }
        return null;
    }
    // Renders an individual piece with its specified image from the pieceFolder
    private void renderPiece(GraphicsContext gc, GamePiece gamePiece) {
        gc.setFill(gamePiece.getGameTeam() == GameTeam.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR);
        gc.drawImage(getPieceImage(gamePiece),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getX() * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + gamePiece.getBoardLocation().getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
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

    private void renderAttackHelp(GraphicsContext gc) {
        if(attackChances[currentCursorPosition.getX()][currentCursorPosition.getY()] != 0) {
            gc.setGlobalAlpha(0.5);
            gc.drawImage(FileLoader.getImage(otherFolder + "/" + "D" + attackChances[currentCursorPosition.getX()][currentCursorPosition.getY()] + ".png"),BOARD_X_OFFSET + BOARD_SQUARE_OFFSET + (currentCursorPosition.getX()) * PIECE_LENGTH,BOARD_Y_OFFSET + BOARD_SQUARE_OFFSET + currentCursorPosition.getY() * PIECE_LENGTH,PIECE_LENGTH, PIECE_LENGTH);
        }
    }

    public void setCurrentCursorPosition(BoardLocation currentCursorPosition) {
        this.currentCursorPosition = currentCursorPosition;
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

    public int[][] getAttackChances() {
        return attackChances;
    }

    public void clearAttackChances() {
        attackChances = new int[gameState.getGameBoard().getBOARD_WIDTH()][gameState.getGameBoard().getBOARD_HEIGHT()];
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
