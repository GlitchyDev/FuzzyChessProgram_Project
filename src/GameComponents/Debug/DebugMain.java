package GameComponents.Debug;

import GameComponents.Board.Pieces.BoardLocation;
import GameComponents.Board.Pieces.GamePiece;
import GameComponents.Board.Turn.Action;
import GameComponents.GameState;

import java.util.ArrayList;

public class DebugMain {

    public static void main(String[] args) {
        GameState originalGamestate = new GameState();
        GamePiece origin = originalGamestate.getGameBoard().getPiece(new BoardLocation(0,1));

        ArrayList<Action> possibleActions = originalGamestate.getValidActions(origin);
        System.out.println("Original");
        System.out.println(originalGamestate);
        System.out.println("------");

        ArrayList<GameState> branches = new ArrayList<>();
        for(Action action: possibleActions) {
            System.out.println("Alt Board State");
            GameState branch = originalGamestate.branchState(action);
            branches.add(branch);
            System.out.println(action);
            System.out.println(branch);
        }
    }
}
