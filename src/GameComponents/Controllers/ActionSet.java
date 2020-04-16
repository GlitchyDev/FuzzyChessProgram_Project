package GameComponents.Controllers;

import GameComponents.Board.Turn.Action;
import GameComponents.GameState;

import java.util.ArrayList;

//Evaluation result for a given game state
//Store/hold all possible actions for each turn
//plus the evaluation of each game state
// RETURN: A list of ActionSet objects
public class ActionSet {
    private final int evaluationResult;
    private final GameState resultantGameState;
    private final ArrayList<Action> actionList;

    public ActionSet(int evaluationResult, GameState resultantGameState) {
        this.evaluationResult = evaluationResult;
        this.resultantGameState = resultantGameState;
        this.actionList = new ArrayList<>();
    }

    public ActionSet(int evaluationResult, GameState resultantGameState, ActionSet previousActionSet, Action action) {
        this.evaluationResult = evaluationResult;
        this.resultantGameState = resultantGameState;
        this.actionList = new ArrayList<>();
        actionList.addAll(previousActionSet.getActionList());
        actionList.add(action);
    }

    public ArrayList<Action> getActionList() {
        return actionList;
    }

    public int getEvaluationResult() {
        return evaluationResult;
    }

    public GameState getResultantGameState() {
        return resultantGameState;
    }
}