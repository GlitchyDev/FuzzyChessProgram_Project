package GameComponents.Controllers;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Pieces.GamePieceType;
import GameComponents.Board.Turn.Action;
import GameComponents.GUIRenderer;
import GameComponents.GameState;


public class AIController {

    static int numOfStepsPerMove = 0;

    private GUIRenderer guiRenderer;
    private Evaluator evaluator = new Evaluator();
    private int aiControllerEvaluationResult = 0;
    private int playerControllerEvaluationResult = 0;

    public void preformAction(GameState originalGameState) {
        numOfStepsPerMove++;
        for(int i = 0; i < originalGameState.getGameBoard().getBlackPieces().size(); i++) {
            GamePiece p = originalGameState.getGameBoard().getBlackPieces().get(i);
            if(originalGameState.getValidActions(p).size() > 1) {
                Action action = originalGameState.getValidActions(p).get(0);
                GameState aiControllerGameState = originalGameState.branchState(action);
                originalGameState.preformAction(action);
                aiControllerEvaluationResult = evaluator.evaluateGameState(aiControllerGameState, p.getGameTeam());
                System.out.println(aiControllerEvaluationResult);

                break;
            }


        }
        numOfStepsPerMove++;
        if(numOfStepsPerMove == 4){
            playerControllerEvaluationResult = evaluator.evaluateGameState(originalGameState,GameTeam.WHITE);
            System.out.println(playerControllerEvaluationResult);
            System.out.println("-----------------------------");
            numOfStepsPerMove = 0;
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
