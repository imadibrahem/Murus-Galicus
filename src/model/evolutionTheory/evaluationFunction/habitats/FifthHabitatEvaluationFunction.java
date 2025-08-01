package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class FifthHabitatEvaluationFunction extends EvaluationFunction {
    public FifthHabitatEvaluationFunction (Board board){
        super(board, new int[]{1, 0, 0, 1, 1, 1, 4}, new int[]{2, 2, 2, 1}, new int[]{6, 6, 1, 1, 4, 3, 2}, new int[]{-2, -1, 1, 3}, new int[]{3, 3, 1, -3, -3, 3, -3, -3}, new int[]{-139, -75, 0, -16, 0, 16, 75, 139}, 26, 17, 0);
    }
    public FifthHabitatEvaluationFunction (){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{1, 0, 0, 1, 1, 1, 4}, new int[]{2, 2, 2, 1}, new int[]{6, 6, 1, 1, 4, 3, 2}, new int[]{-2, -1, 1, 3}, new int[]{3, 3, 1, -3, -3, 3, -3, -3}, new int[]{-139, -75, 0, -16, 0, 16, 75, 139}, 26, 17, 0);
    }
}
