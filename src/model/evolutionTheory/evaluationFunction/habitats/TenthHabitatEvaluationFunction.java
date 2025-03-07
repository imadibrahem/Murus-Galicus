package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class TenthHabitatEvaluationFunction extends EvaluationFunction {
    public TenthHabitatEvaluationFunction (Board board){
        super(board, new int[]{2, 3, 2, 1, 1, 2, 0}, new int[]{0, 2, 2, 1}, new int[]{2, 7, 0, 2, 18, 13, 6}, new int[]{3, 3, 3, 11}, new int[]{3, -1, 2, 2, 0, 1, 3, 2}, new int[]{-24, -135, 0, -67, 0, 67, 135, 24}, 32, 29, 8);
    }
    public TenthHabitatEvaluationFunction (){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{2, 3, 2, 1, 1, 2, 0}, new int[]{0, 2, 2, 1}, new int[]{2, 7, 0, 2, 18, 13, 6}, new int[]{3, 3, 3, 11}, new int[]{3, -1, 2, 2, 0, 1, 3, 2}, new int[]{-24, -135, 0, -67, 0, 67, 135, 24}, 32, 29, 8);
    }
}
