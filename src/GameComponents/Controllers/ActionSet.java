package GameComponents.Controllers;

import GameComponents.Board.Turn.Action;
import GameComponents.GameState;

import java.util.ArrayList;

//Score
//Generate all possible actions for each turn
//Evaluate and store the score
// RETURN: This object with a getter method
public class ActionSet {
    private final int value;
    private final GameState resultantGameState;
    private final ArrayList<Action> actionList;

    public ActionSet(int value, GameState resultantGameState) {
        this.value = value;
        this.resultantGameState = resultantGameState;
        this.actionList = new ArrayList<>();
    }

    public ActionSet(int value, GameState resultantGameState, ActionSet previousActionSet, Action action) {
        this.value = value;
        this.resultantGameState = resultantGameState;
        this.actionList = new ArrayList<>();
        actionList.addAll(previousActionSet.getActionList());
        actionList.add(action);
    }

    public ArrayList<Action> getActionList() {
        return actionList;
    }

    public int getValue() {
        return value;
    }

    public GameState getResultantGameState() {
        return resultantGameState;
    }
}
