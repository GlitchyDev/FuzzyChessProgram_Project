package GameComponents.Controllers;

import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.GameState;

public class AIController {



    public void onTurnStartPhase(GameState gameState) {

        for(int i = 0; i < gameState.getGameBoard().getBlackPieces().size(); i++) {
            GamePiece p = gameState.getGameBoard().getBlackPieces().get(i);
            if(gameState.getValidActions(p).size() > 1) {
                Action action = gameState.getValidActions(p).get(0);
                gameState.preformAction(action);

                GameState theRightTimeLine = gameState.branchState(action);


                break;
            }

            //

        }
    }

}
