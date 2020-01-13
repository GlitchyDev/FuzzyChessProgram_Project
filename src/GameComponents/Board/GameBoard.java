package GameComponents.Board;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;

public class GameBoard {
    private final int BOARD_WIDTH = 8;
    private final int BOARD_HEIGHT = 8;

    private final GamePiece[][] gameBoard;


    public GameBoard() {
        gameBoard = new GamePiece[BOARD_WIDTH][BOARD_HEIGHT];
        populate();
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
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.KING, x, y);
                                break;
                            case 7:
                                gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.ROOK, x, y);
                                break;
                        }
                        break;
                    case 1:
                        gameBoard[x][y] = new GamePiece(GameTeam.BLACK, GamePieceType.PAWN, x, y);
                        break;
                    // White
                    case 6:
                        gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.PAWN, x, y);
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
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.KING, x, y);
                                break;
                            case 7:
                                gameBoard[x][y] = new GamePiece(GameTeam.WHITE, GamePieceType.ROOK, x, y);
                                break;
                        }
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

    public boolean isInsideBoard(int x, int y) {
        return isInsideBoard(new BoardLocation(x,y));
    }

    public boolean isInsideBoard(BoardLocation boardLocation) {
        return boardLocation.getX() >= 0 && boardLocation.getY() < BOARD_WIDTH && boardLocation.getY() >= 0 && boardLocation.getY() < BOARD_HEIGHT;
    }


    @Override
    public String toString() {
        String boardString = "";
        for(int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                boardString += "[";
                if(isSpaceOccupied(x,y)) {
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
                    boardString += " ";
                }
                boardString += "]";
            }
            boardString += "\n";
        }

        return boardString;
    }
}
