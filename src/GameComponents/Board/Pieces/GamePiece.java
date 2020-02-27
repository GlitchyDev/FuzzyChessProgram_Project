package GameComponents.Board.Pieces;

import GameComponents.Board.GameTeam;

/**
 * A Object Representation of a Gamepiece
 */
public class GamePiece {
    private final GameTeam gameTeam;
    private final GamePieceType gamePieceType;
    private BoardLocation boardLocation;

    public GamePiece(GameTeam gameTeam, GamePieceType gamePieceType, int x, int y) {
        this.gameTeam = gameTeam;
        this.gamePieceType = gamePieceType;
        this.boardLocation = new BoardLocation(x, y);
    }

    public GamePiece(GameTeam gameTeam, GamePieceType gamePieceType, BoardLocation boardLocation) {
        this.gameTeam = gameTeam;
        this.gamePieceType = gamePieceType;
        this.boardLocation = boardLocation;
    }

    public GameTeam getGameTeam() {
        return gameTeam;
    }

    public GamePieceType getGamePieceType() {
        return gamePieceType;
    }

    public BoardLocation getBoardLocation() {
        return boardLocation;
    }

    public void setBoardLocation(BoardLocation boardLocation) {
        this.boardLocation = boardLocation;
    }

    public GamePiece clone() {
        return new GamePiece(gameTeam,gamePieceType,getBoardLocation());
    }

    @Override
    public String toString() {
        return "[" + gameTeam + " " + gamePieceType + "]";
    }
}
