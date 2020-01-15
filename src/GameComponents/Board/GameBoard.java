package GameComponents.Board;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;

import java.util.ArrayList;

public class GameBoard {
    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;
    private final GamePiece[][] gameBoard;
    private final ArrayList<GamePiece> whitePieces;
    private final ArrayList<GamePiece> blackPieces;


    public GameBoard() {
        gameBoard = new GamePiece[BOARD_WIDTH][BOARD_HEIGHT];
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        populate();
    }

    public GameBoard(ArrayList<GamePiece> whitePieces, ArrayList<GamePiece> blackPieces) {
        gameBoard = new GamePiece[BOARD_WIDTH][BOARD_HEIGHT];
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
        for(GamePiece gamePiece: whitePieces) {
            addPiece(gamePiece.clone(),gamePiece.getBoardLocation());
        }
        for(GamePiece gamePiece: blackPieces) {
            addPiece(gamePiece.clone(),gamePiece.getBoardLocation());
        }
    }

    public void populate() {
        for(int y = 0; y < BOARD_HEIGHT; y++) {
            for(int x = 0; x < BOARD_WIDTH; x++) {
                switch(y) {
                    // Black
                    case 0:
                        switch(x) {
                            case 0:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.ROOK, x, y);
                                break;
                            case 1:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.KNIGHT, x, y);
                                break;
                            case 2:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.BISHOP, x, y);
                                break;
                            case 3:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.QUEEN, x, y);
                                break;
                            case 4:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.KING, x, y);
                                break;
                            case 5:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.BISHOP, x, y);
                                break;
                            case 6:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.KNIGHT, x, y);
                                break;
                            case 7:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.ROOK, x, y);
                                break;
                        }
                        blackPieces.add(gameBoard[x][y]);
                        break;
                    case 1:
                        gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.PAWN, x, y);
                        blackPieces.add(gameBoard[x][y]);
                        break;
                    // White
                    case 6:
                        gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.PAWN, x, y);
                        whitePieces.add(gameBoard[x][y]);
                        break;
                    case 7:
                        switch(x) {
                            case 0:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.ROOK, x, y);
                                break;
                            case 1:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.KNIGHT, x, y);
                                break;
                            case 2:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.BISHOP, x, y);
                                break;
                            case 3:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.KING, x, y);
                                break;
                            case 4:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.QUEEN, x, y);
                                break;
                            case 5:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.BISHOP, x, y);
                                break;
                            case 6:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.KNIGHT, x, y);
                                break;
                            case 7:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.ROOK, x, y);
                                break;
                        }
                        whitePieces.add(gameBoard[x][y]);
                        break;
                }
            }
        }
    }

    public boolean isSpaceOccupied(int x, int y) {
        return isSpaceOccupied(new BoardLocation(x,y));
    }

    public boolean isSpaceOccupied(BoardLocation boardLocation) {
        return gameBoard[boardLocation.getX()][boardLocation.getY()] != null;
    }

    public GamePiece getPiece(int x, int y) {
        return getPiece(new BoardLocation(x,y));
    }

    public GamePiece getPiece(BoardLocation boardLocation) {
        return gameBoard[boardLocation.getX()][boardLocation.getY()];
    }

    public void deletePiece(int x, int y) {
        deletePiece(new BoardLocation(x,y));
    }

    public void deletePiece(BoardLocation boardLocation) {
        GamePiece gamePiece = getPiece(boardLocation);
        if(gamePiece.getGameTeam() == GameTeam.WHITE) {
            whitePieces.remove(gamePiece);
        } else {
            blackPieces.remove(gamePiece);
        }
        gameBoard[boardLocation.getX()][boardLocation.getY()] = null;
    }

    public void addPiece(GamePiece gamePiece, int x, int y) {
        addPiece(gamePiece, new BoardLocation(x,y));
    }

    public void addPiece(GamePiece gamePiece, BoardLocation boardLocation) {
        gameBoard[boardLocation.getX()][boardLocation.getY()] = gamePiece;
        if(gamePiece.getGameTeam() == GameTeam.WHITE) {
            whitePieces.add(gamePiece);
        } else {
            blackPieces.add(gamePiece);
        }
    }

    public boolean isInsideBoard(int x, int y) {
        return isInsideBoard(new BoardLocation(x,y));
    }

    public boolean isInsideBoard(BoardLocation boardLocation) {
        return boardLocation.getX() >= 0 && boardLocation.getX() < BOARD_WIDTH && boardLocation.getY() >= 0 && boardLocation.getY() < BOARD_HEIGHT;
    }

    public void movePiece(int x1, int y1, int x2, int y2) {
        movePiece(new BoardLocation(x1,y1),new BoardLocation(x2,y2));
    }

    public void movePiece(BoardLocation oldLocation, BoardLocation newLocation) {
        GamePiece gamePiece = getPiece(oldLocation);
        deletePiece(oldLocation);
        addPiece(gamePiece,newLocation);
    }

    public ArrayList<GamePiece> retrieveValidPieces(GameTeam gameTeam, GamePieceType gamePieceType) {
        ArrayList<GamePiece> validPieces = new ArrayList<>();
        if(gameTeam == GameTeam.WHITE) {
            for(GamePiece gamePiece: whitePieces) {
                if(gamePiece.getGamePieceType() == gamePieceType) {
                    validPieces.add(gamePiece);
                }
            }
        } else {
            for(GamePiece gamePiece: blackPieces) {
                if(gamePiece.getGamePieceType() == gamePieceType) {
                    validPieces.add(gamePiece);
                }
            }
        }
        return validPieces;
    }

    public ArrayList<GamePiece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<GamePiece> getBlackPieces() {
        return blackPieces;
    }

    public GameBoard clone() {
        return new GameBoard(whitePieces,blackPieces);
    }

    @Override
    public String toString() {
        String boardString = "";
        for(int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if(isSpaceOccupied(x,y)) {
                    if(getPiece(x,y).getGameTeam() == GameTeam.WHITE) {
                        boardString += "{";
                    }
                    if(getPiece(x,y).getGameTeam() == GameTeam.BLACK) {
                        boardString += "(";
                    }
                    switch(getPiece(x,y).getGamePieceType()) {
                        case KING:
                            boardString += "K";
                            break;
                        case QUEEN:
                            boardString += "Q";
                            break;
                        case ROOK:
                            boardString += "R";
                            break;
                        case BISHOP:
                            boardString += "B";
                            break;
                        case KNIGHT:
                            boardString += "N";
                            break;
                        case PAWN:
                            boardString += "P";
                            break;
                    }
                } else {
                    boardString += "[ ";
                }
                if(isSpaceOccupied(x,y)) {
                    if(getPiece(x,y).getGameTeam() == GameTeam.WHITE) {
                        boardString += "}";
                    }
                    if(getPiece(x,y).getGameTeam() == GameTeam.BLACK) {
                        boardString += ")";
                    }
                } else {
                    boardString += "]";
                }
            }
            boardString += "\n";
        }

        return boardString;
    }
}
