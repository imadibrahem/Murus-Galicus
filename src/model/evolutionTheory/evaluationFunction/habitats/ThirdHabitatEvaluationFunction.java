package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class ThirdHabitatEvaluationFunction extends EvaluationFunction {
    public ThirdHabitatEvaluationFunction (Board board){
        super(board, new int[]{0, 2, 3, 6, 0, 0, 0}, new int[]{5, 2, 1, -1}, new int[]{0, 3, 0, 1, 3, 3, 3}, new int[]{1, 2, 8, 2}, new int[]{0, -4, -4, 4, 2, 5, -2, 8}, new int[]{-505, -70, 0, -51, 0, 51, 70, 505}, 38, 71, 3);
    }
    public ThirdHabitatEvaluationFunction (){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{0, 2, 3, 6, 0, 0, 0}, new int[]{5, 2, 1, -1}, new int[]{0, 3, 0, 1, 3, 3, 3}, new int[]{1, 2, 8, 2}, new int[]{0, -4, -4, 4, 2, 5, -2, 8}, new int[]{-505, -70, 0, -51, 0, 51, 70, 505}, 38, 71, 3);
    }
}
