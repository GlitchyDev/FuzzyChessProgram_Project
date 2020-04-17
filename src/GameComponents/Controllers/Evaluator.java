package GameComponents.Controllers;

import GameComponents.Board.GameBoard;
import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.GamePieceType;
import GameComponents.Board.Turn.Action;
import GameComponents.Board.Turn.AttackAction;
import GameComponents.GameState;
import GameComponents.Board.Pieces.GamePiece;

import java.util.*;
import java.lang.Math;

import static GameComponents.Board.GameTeam.*;

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
        GameTeam opponent;
        switch(currentTeam) {
            case BLACK:
                opponent = WHITE;
                break;
            case WHITE:
                opponent = BLACK;
                break;
            case BLACK_WIN:
                opponent = WHITE;
                break;
            case WHITE_WIN:
                opponent = BLACK;
                break;
        }
        int opponentValue = 0;

        if(currentTeam == WHITE){
            //System.out.print(currentTeam + ": ");
            playerValue = getValueOfPieces(gameState.getGameBoard().getWhitePieces(), gameState);
            opponentValue = getValueOfPieces(gameState.getGameBoard().getBlackPieces(), gameState);
        }
        else if(currentTeam == BLACK) {
            //System.out.print(currentTeam + ": ");
            playerValue = getValueOfPieces(gameState.getGameBoard().getBlackPieces(), gameState);
            opponentValue = getValueOfPieces(gameState.getGameBoard().getWhitePieces(), gameState);
        }

        return playerValue - opponentValue;
    }

    private int getValueOfPieces(ArrayList<GamePiece> pieces, GameState gameState) {
        int value = 0;

        for(GamePiece piece : pieces) {

            value += getValueOfAPiece(piece, gameState);
        }

        return value;
    }

    private int getValueOfAPiece(GamePiece piece,  GameState gameState) { //Add game state as a parameter

        int value = 0;
        if(piece.getGameTeam() == GameTeam.WHITE){
            ArrayList<Action> possibleActions = gameState.getValidActions(piece);
            if(possibleActions.size() > 0){
                for(Action action: possibleActions){
                    if(action instanceof AttackAction){
                        GamePiece targetPiece = ((AttackAction) action).getTargetPiece();
                        value += ((int)Math.round((piece.getGamePieceType().getSuccessAttackChance(targetPiece.getGamePieceType())*getValueOfAPiece(targetPiece,gameState))));
                    }
                }

            }
        }
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
        return value;
    }

}
