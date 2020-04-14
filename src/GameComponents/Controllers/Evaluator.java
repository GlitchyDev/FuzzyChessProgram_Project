package GameComponents.Controllers;

import GameComponents.Board.GameBoard;
import GameComponents.Board.GameTeam;
import GameComponents.GUIRenderer;
import GameComponents.GameState;
import GameComponents.Board.Pieces.GamePiece;

import java.util.ArrayList;

import static GameComponents.Board.GameTeam.WHITE;
import static GameComponents.Board.GameTeam.BLACK;

public class Evaluator {

    private static final int kingval = 20000;
    private static final int queenval = 900;
    private static final int rookval = 500;
    private static final int bishopval = 330;
    private static final int knightval = 320;
    private static final int pawnval = 100;

    /**
     *
     * @param gameState
     * @param currentTeam THE TEAM WE WANT TO EVALUATE FOR!!
     * @return
     */
    public int evaluateGameState(GameState gameState, GameTeam currentTeam) {

        int playerValue = 0;

        // Opponent is opposite of
        GameTeam opponent = (currentTeam == WHITE) ? BLACK : WHITE;
        int opponentValue = 0;


        if(currentTeam == WHITE){
            System.out.print(currentTeam + ": ");
            playerValue = getValueOfPieces(gameState.getGameBoard().getWhitePieces(),gameState);
            opponentValue = getValueOfPieces(gameState.getGameBoard().getBlackPieces(),gameState);
        }
        else{
            System.out.print(currentTeam + ": ");
            playerValue = getValueOfPieces(gameState.getGameBoard().getBlackPieces(),gameState);
            opponentValue = getValueOfPieces(gameState.getGameBoard().getWhitePieces(),gameState);
        }

        return playerValue - opponentValue;
    }

    private int getValueOfPieces(ArrayList<GamePiece> pieces, GameState gameState) {

        int value = 0;
        for(GamePiece piece : pieces) {
            switch(piece.getGamePieceType()) {
                case PAWN:
                    value += pawnval;
                    break;
                case BISHOP:
                    value += bishopval;
                    break;
                case KNIGHT:
                    value += knightval;
                    break;
                case ROOK:
                    value += rookval;
                    break;
                case QUEEN:
                    value += queenval;
                    break;
                case KING:
                    value += kingval;
                    break;
            }
        }
        // Get all actions of the piece
        // Get the Attacks
        // Add a value for each attack based on what piece is being attacked


        return value;
    }

}
