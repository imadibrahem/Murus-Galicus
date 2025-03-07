package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class SeventhHabitatEvaluationFunction extends EvaluationFunction {
    public SeventhHabitatEvaluationFunction (Board board){
        super(board, new int[]{2, 0, 2, 1, 2, 2, 2}, new int[]{1, 5, 1, 2}, new int[]{6, 14, 5, 4, 5, 2, 5}, new int[]{1, 0, 3, 5}, new int[]{9, 3, 1, 3, -5, 1, 11, 6}, new int[]{-118, -204, 0, -34, 0, 34, 204, 118}, 26, 32, 2);
    }
    public SeventhHabitatEvaluationFunction (){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{2, 0, 2, 1, 2, 2, 2}, new int[]{1, 5, 1, 2}, new int[]{6, 14, 5, 4, 5, 2, 5}, new int[]{1, 0, 3, 5}, new int[]{9, 3, 1, 3, -5, 1, 11, 6}, new int[]{-118, -204, 0, -34, 0, 34, 204, 118}, 26, 32, 2);
    }
}
