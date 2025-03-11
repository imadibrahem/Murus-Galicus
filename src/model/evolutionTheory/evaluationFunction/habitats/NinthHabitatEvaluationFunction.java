package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class NinthHabitatEvaluationFunction  extends EvaluationFunction {
    public NinthHabitatEvaluationFunction (Board board){
        super(board, new int[]{4, 10, 8, 2, 0, 0, 1}, new int[]{-1, -1, 1, 1}, new int[]{16, 2, 2, 2, 2, 3, 0}, new int[]{1, 1, 1, 4}, new int[]{5, 4, -1, 6, 2, 0, 3, 4}, new int[]{-345, -262, 0, -140, 0, 140, 262, 345}, 34, 49, 8);
    }
    public NinthHabitatEvaluationFunction (){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{4, 10, 8, 2, 0, 0, 1}, new int[]{-1, -1, 1, 1}, new int[]{16, 2, 2, 2, 2, 3, 0}, new int[]{1, 1, 1, 4}, new int[]{5, 4, -1, 6, 2, 0, 3, 4}, new int[]{-345, -262, 0, -140, 0, 140, 262, 345}, 34, 49, 8);
    }
}
