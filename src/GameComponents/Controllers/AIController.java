package GameComponents.Controllers;

import GameComponents.Board.GameTeam;
import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.Board.Turn.AttackAction;
import GameComponents.Board.Turn.NothingAction;
import GameComponents.GUIRenderer;
import GameComponents.GameState;
import java.util.ArrayList;
import java.util.Arrays;

import static GameComponents.Board.GameTeam.BLACK;
import static GameComponents.Board.GameTeam.WHITE;

public class AIController {

    private GUIRenderer guiRenderer;
    private Evaluator evaluator = new Evaluator();

    private final int N_MAX = 2;
    private final int LAYERS_DEEP = 4;
    public void preformAction(GameState originalGameState) {

        ArrayList<ActionSet> originalActionSet = new ArrayList<>();
        originalActionSet.add(new ActionSet(evaluator.evaluateGameState(originalGameState,originalGameState.getCurrentTeamTurn()),originalGameState));
        System.out.println(" ");

        ArrayList<ActionSet> bestActions = new ArrayList<>();
        bestActions.addAll(originalActionSet);
        for(int i = 0; i < LAYERS_DEEP; i++) {
            bestActions = getTopNActions(originalGameState.getCurrentTeamTurn(), bestActions);
        }

        if(bestActions.size() > 0 && bestActions.get(0).getActionList().size() > 0) {
            originalGameState.preformAction(getBestAction(bestActions).getActionList().get(0));
        } else {
            originalGameState.preformAction(new NothingAction(null));
        }
        afterTurn();
        // Find best value out of best actions list, grab the action set, look at the array index 0 and 1, do both actions, call afterTurn

    }


    public ActionSet getBestAction(ArrayList<ActionSet> actionSets) {
        ActionSet maxAction = null;
        for(ActionSet actionset: actionSets) {
            if(actionset != null) {
                if (maxAction == null || maxAction.getEvaluationResult() < actionset.getEvaluationResult()) {
                    maxAction = actionset;
                }
            }
        }
        return maxAction;
    }

    /**
     * It will return the top N_Max Actionset from the provided actionsets
     * @return
     */
    public ArrayList<ActionSet> getTopNActions(GameTeam evaluatorsTeam, ArrayList<ActionSet> providedActionSet) {
        ArrayList<ActionSet> resultantActions = new ArrayList<>();
        //ActionSet[] topActionSets = new ActionSet[N_MAX];

        for(ActionSet actionSet: providedActionSet) {
            System.out.println("ActionSet size = " + providedActionSet.size());
            if (actionSet != null && actionSet.getResultantGameState() != null && actionSet.getResultantGameState().getCurrentTeamTurn() != null) {
                GameTeam gameTeam = actionSet.getResultantGameState().getCurrentTeamTurn();
                ActionSet[] topActionSets = new ActionSet[N_MAX];
                // You want to go through every gamepiece of the current players turn, hint, you can cheat and look at whose turn it is
                ArrayList<GamePiece> gamePieces;
                switch(actionSet.getResultantGameState().getCurrentTeamTurn()) {
                    case BLACK:
                        gamePieces = actionSet.getResultantGameState().getGameBoard().getBlackPieces();
                        break;
                    case WHITE:
                        gamePieces = actionSet.getResultantGameState().getGameBoard().getWhitePieces();
                        break;
                    case BLACK_WIN:
                        gamePieces = actionSet.getResultantGameState().getGameBoard().getBlackPieces();
                        break;
                    case WHITE_WIN:
                        gamePieces = actionSet.getResultantGameState().getGameBoard().getWhitePieces();
                        break;
                    default:
                        gamePieces = null;
                }
                for (GamePiece gamePiece : gamePieces) {
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
                for(int i = 0; i < N_MAX; i++) {
                    if(topActionSets[i] != null) {
                        resultantActions.add(topActionSets[i]);
                    } else {
                        resultantActions.add(new ActionSet(actionSet.getEvaluationResult(), actionSet.getResultantGameState(), actionSet, new NothingAction(null)));
                    }
                }
            }
        }
        System.out.println("--------------->resultantActions size = " + resultantActions.size());

        return resultantActions;
    }

    public void checkGreater(ActionSet[] topActionSet, ActionSet actionSet) {
        if(actionSet != null) {
            for (int i = 0; i < topActionSet.length; i++) {
                if (topActionSet[i] == null || actionSet.getEvaluationResult() > topActionSet[i].getEvaluationResult()) {
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
                if (topActionSet[i] == null || actionSet.getEvaluationResult() < topActionSet[i].getEvaluationResult()) {
                    ActionSet oldActionSet = topActionSet[i];
                    topActionSet[i] = actionSet;
                    checkGreater(topActionSet, oldActionSet);
                    return;
                }
            }
        }
    }


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