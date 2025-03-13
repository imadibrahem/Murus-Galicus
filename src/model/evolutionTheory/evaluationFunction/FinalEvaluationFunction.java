package model.evolutionTheory.evaluationFunction;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class FinalEvaluationFunction extends EvaluationFunction {
    public FinalEvaluationFunction(Board board) {
        super(board, new int[]{1, 1, 2, 1, 0, 0, 0}, new int[]{2, 0, -1, 1}, new int[]{1, 7, 3, 3, 4, 3, 5}, new int[]{4, 1, -2, 0}, new int[]{5, 4, 3, 3, 0, 2, 3, 6}, new int[]{-350, -173, 0, -8, 0, 8, 173, 350}, 29, 35, 2);
    }
    public FinalEvaluationFunction(){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{1, 1, 2, 1, 0, 0, 0}, new int[]{2, 0, -1, 1}, new int[]{1, 7, 3, 3, 4, 3, 5}, new int[]{4, 1, -2, 0}, new int[]{5, 4, 3, 3, 0, 2, 3, 6}, new int[]{-350, -173, 0, -8, 0, 8, 173, 350}, 29, 35, 2);
    }
}
