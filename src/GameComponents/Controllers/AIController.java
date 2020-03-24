package GameComponents.Controllers;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.Board.Turn.AttackAction;
import GameComponents.GUIRenderer;
import GameComponents.GameState;
import java.util.ArrayList;
import java.util.Arrays;

public class AIController {

    private GUIRenderer guiRenderer;
    private Evaluator evaluator = new Evaluator();
    //private int aiControllerEvaluationResult = 0;
    //private int playerControllerEvaluationResult = 0;
    //Action bestAction;
    //ArrayList<Action> futureActions = new ArrayList<>();
    //ArrayList<Action> oponentActions = new ArrayList<>();

    private final int N_MAX = 3;
    private final int LAYERS_DEEP = 3;
    public void preformAction(GameState originalGameState) {
        ArrayList<ActionSet> originalActionSet = new ArrayList<>();
        originalActionSet.add(new ActionSet(evaluator.evaluateGameState(originalGameState,originalGameState.getCurrentTeamTurn()),originalGameState));

        ArrayList<ActionSet> bestActions = new ArrayList<>();
        bestActions.addAll(originalActionSet);
        for(int i = 0; i < LAYERS_DEEP; i++) {
            ArrayList<ActionSet> topNActions = getTopNActions(originalGameState.getCurrentTeamTurn(), bestActions);
            if(topNActions.size() >= 1) {
                bestActions = getTopNActions(originalGameState.getCurrentTeamTurn(), bestActions);
            } else {
                System.out.println("Early Terminate");
                break;
            }
        }

        System.out.println("Bees");
        if(bestActions.get(0).getActionList().size() > 0) {
            originalGameState.preformAction(bestActions.get(0).getActionList().get(0));
        }
        afterTurn();
        // Find best value out of best actions list, grab the action set, look at the array index 0 and 1, do both actions, call afterTurn

        /*
        if(originalGameState.getTurnActions(originalGameState.getCurrentTurnNumber()).size() == 0  ||
                originalGameState.getTurnActions(originalGameState.getCurrentTurnNumber()).size() == 1) {
            Action bestAction = selectBestAction(originalGameState);
            originalGameState.preformAction(bestAction);
            afterTurn();
        }
         */
    }


    /**
     * It will return the top N_Max Actionset from the provided actionsets
     * @return
     */
    public ArrayList<ActionSet> getTopNActions(GameTeam evaluatorsTeam, ArrayList<ActionSet> providedActionSet) {
        ArrayList<ActionSet> resultantActions = new ArrayList<>();
        for(ActionSet actionSet: providedActionSet) {
            GameTeam gameTeam = actionSet.getResultantGameState().getCurrentTeamTurn();
            ActionSet[] topActionSets = new ActionSet[N_MAX];
            // You want to go through every gamepiece of the current players turn, hint, you can cheat and look at whose turn it is
            if(actionSet.getResultantGameState().getGameBoard().getWhitePieces().size() != 0 &&  actionSet.getResultantGameState().getGameBoard().getBlackPieces().size() != 0) {
                for (GamePiece gamePiece : gameTeam == GameTeam.WHITE ? actionSet.getResultantGameState().getGameBoard().getWhitePieces() : actionSet.getResultantGameState().getGameBoard().getBlackPieces()) {
                    for (Action potentialAction : actionSet.getResultantGameState().getValidActions(gamePiece)) {
                        if(potentialAction instanceof AttackAction) {
                            System.out.println("Bees");
                        }
                        GameState potentialGameState = actionSet.getResultantGameState().branchState(potentialAction);
                        int value = evaluator.evaluateGameState(potentialGameState, gameTeam);

                        ActionSet potentialActionSet = new ActionSet(value, potentialGameState, actionSet, potentialAction);
                        // If its your turn, you do checkGreater, if its your oponents you do check lesser
                        if (gameTeam == evaluatorsTeam) {
                            checkGreater(topActionSets, potentialActionSet);
                        } else {
                            checkLesser(topActionSets, potentialActionSet);
                        }
                    }
                }
                for(ActionSet topAction: topActionSets) {
                    if(topAction != null) {
                        resultantActions.add(topAction);
                    }
                }
            }
        }


        return resultantActions;
    }

    public void checkGreater(ActionSet[] topActionSet, ActionSet actionSet) {
        if(actionSet != null) {
            for (int i = 0; i < topActionSet.length; i++) {
                if (topActionSet[i] == null || actionSet.getValue() > topActionSet[i].getValue()) {
                    ActionSet oldActionSet = topActionSet[i];
                    topActionSet[i] = actionSet;
                    checkGreater(topActionSet, oldActionSet);
                    return;
                }
            }
        }

    }
    public void checkLesser(ActionSet[] topActionSet, ActionSet actionSet) {
        if(actionSet != null) {
            for (int i = 0; i < topActionSet.length; i++) {
                if (topActionSet[i] == null || actionSet.getValue() < topActionSet[i].getValue()) {
                    ActionSet oldActionSet = topActionSet[i];
                    topActionSet[i] = actionSet;
                    checkGreater(topActionSet, oldActionSet);
                    return;
                }
            }
        }
    }


    /*
    //Select best n_max actions from the BestAction object
    //Call selectBestAction again
    private Action selectBestAction(GameState nextGameState){

        for(int i = 0; i < nextGameState.getGameBoard().getBlackPieces().size(); i++) {

            GamePiece p = nextGameState.getGameBoard().getBlackPieces().get(i);
            futureActions = nextGameState.getValidActions(p);

            System.out.println("-----------------------------");
            System.out.println("Piece Type: " + p.getGamePieceType());
            System.out.println("Num of Actions = " + futureActions.size());

            for (int j = 0; j < futureActions.size(); j++) {

                System.out.println("Action Type = " + futureActions.get(j).getActionType());
                GameState aiControllerBranchGameState = nextGameState.branchState(futureActions.get(j));
                aiControllerEvaluationResult = evaluator.evaluateGameState(aiControllerBranchGameState, p.getGameTeam());
                System.out.println(aiControllerEvaluationResult);
            }
            System.out.println("-----------------------------");

        }
        if(nextGameState.getTurnActions(nextGameState.getCurrentTurnNumber()).size() == 0) {
            return futureActions.get(2);
        }

        GamePiece pf = nextGameState.getGameBoard().getBlackPieces().get(8);
        Action act = nextGameState.getValidActions(pf).get(0);

        return act;
    }

     */

    private void afterTurn() {
        guiRenderer.getSelectedPieces().clear();
        guiRenderer.getSelectedAttackAreas().clear();
        guiRenderer.getSelectedMoveAreas().clear();
        guiRenderer.clearAttackChances();
    }
    public GUIRenderer getGuiRenderer() {
        return guiRenderer;
    }

    public void setGuiRenderer(GUIRenderer guiRenderer) {
        this.guiRenderer = guiRenderer;
    }
}
