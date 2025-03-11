package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class SixthHabitatEvaluationFunction extends EvaluationFunction {
    public SixthHabitatEvaluationFunction (Board board){
        super(board, new int[]{1, 1, 2, 1, 1, 2, 3}, new int[]{0, -1, 0, 1}, new int[]{0, 3, 15, 3, 1, 4, 8}, new int[]{5, 3, 2, 0}, new int[]{1, -2, 1, 4, 3, 1, -1, 2}, new int[]{-552, -246, 0, -24, 0, 24, 246, 552}, 28, 37, 3);
    }
    public SixthHabitatEvaluationFunction (){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{1, 1, 2, 1, 1, 2, 3}, new int[]{0, -1, 0, 1}, new int[]{0, 3, 15, 3, 1, 4, 8}, new int[]{5, 3, 2, 0}, new int[]{1, -2, 1, 4, 3, 1, -1, 2}, new int[]{-552, -246, 0, -24, 0, 24, 246, 552}, 28, 37, 3);
    }
}
