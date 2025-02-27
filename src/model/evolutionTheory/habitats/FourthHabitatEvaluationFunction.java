package model.evolutionTheory.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class FourthHabitatEvaluationFunction extends EvaluationFunction {
    public FourthHabitatEvaluationFunction(Board board) {
        super(board, new int[]{2, 0, 3, 1, 0, 0, 2}, new int[]{3, -1, 2, 1}, new int[]{3, 11, 9, 3, 1, 11, 1}, new int[]{3, 1, 0, 2}, new int[]{4, 9, -1, 2, 2, -1, 3, 2}, new int[]{-101, -155, 0, -141, 0, 141, 155, 101}, 37, 64, 11);
    }
    public FourthHabitatEvaluationFunction() {
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{2, 0, 3, 1, 0, 0, 2}, new int[]{3, -1, 2, 1}, new int[]{3, 11, 9, 3, 1, 11, 1}, new int[]{3, 1, 0, 2}, new int[]{4, 9, -1, 2, 2, -1, 3, 2}, new int[]{-101, -155, 0, -141, 0, 141, 155, 101}, 37, 64, 11);
    }
}
