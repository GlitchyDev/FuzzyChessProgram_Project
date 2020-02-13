package GameComponents.Controllers;

import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.GUIRenderer;
import GameComponents.GameState;

public class AIController {
    private GUIRenderer guiRenderer;



    public void onTurnStartPhase(GameState gameState) {

        for(int i = 0; i < gameState.getGameBoard().getBlackPieces().size(); i++) {
            GamePiece p = gameState.getGameBoard().getBlackPieces().get(i);
            if(gameState.getValidActions(p).size() > 1) {
                Action action = gameState.getValidActions(p).get(0);
                gameState.preformAction(action);

                //GameState theRightTimeLine = gameState.branchState(action);


                break;
            }

            //

        }
        afterTurn();
    }

    private void afterTurn() {
        guiRenderer.getSelectedPieces().clear();
        guiRenderer.getSelectedAttackAreas().clear();
        guiRenderer.getSelectedMoveAreas().clear();
    }


    public GUIRenderer getGuiRenderer() {
        return guiRenderer;
    }

    public void setGuiRenderer(GUIRenderer guiRenderer) {
        this.guiRenderer = guiRenderer;
    }
}
