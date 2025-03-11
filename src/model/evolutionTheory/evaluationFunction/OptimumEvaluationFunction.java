package model.evolutionTheory.evaluationFunction;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class OptimumEvaluationFunction extends EvaluationFunction {
    public OptimumEvaluationFunction(Board board){
        super(board, new int[]{1, 9, 0, 11, 0, 8, 4}, new int[]{13, 7, 1, 1}, new int[]{4, 2, 8, 0, 1, 4, 0}, new int[]{-3, 2, 6, 0}, new int[]{-2, 1, 3, 1, 2, 2, 3, 5}, new int[]{-300, -196, 0, -109, 0, 109, 196, 300}, 33, 34, 0);
    }
    public OptimumEvaluationFunction(){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{1, 9, 0, 11, 0, 8, 4}, new int[]{13, 7, 1, 1}, new int[]{4, 2, 8, 0, 1, 4, 0}, new int[]{-3, 2, 6, 0}, new int[]{-2, 1, 3, 1, 2, 2, 3, 5}, new int[]{-300, -196, 0, -109, 0, 109, 196, 300}, 33, 34, 0);

    }
}
