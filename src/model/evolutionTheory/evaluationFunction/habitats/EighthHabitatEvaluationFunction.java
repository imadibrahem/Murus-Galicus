package model.evolutionTheory.evaluationFunction.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;

public class EighthHabitatEvaluationFunction extends EvaluationFunction {
    public EighthHabitatEvaluationFunction (Board board){
        super(board, new int[]{2, 1, 3, 0, 2, 0, 0}, new int[]{0, -2, 3, 1}, new int[]{0, 2, 2, 2, 12, 3, 3}, new int[]{4, 9, -4, 0}, new int[]{0, 5, 0, 0, 3, 1, 1, 0}, new int[]{-683, -102, 0, -62, 0, 62, 102, 683}, 16, 21, 0);
    }

    public EighthHabitatEvaluationFunction(){
        super(new BitBoard("tttttttt/8/8/8/8/8/TTTTTTTT,b"), new int[]{2, 1, 3, 0, 2, 0, 0}, new int[]{0, -2, 3, 1}, new int[]{0, 2, 2, 2, 12, 3, 3}, new int[]{4, 9, -4, 0}, new int[]{0, 5, 0, 0, 3, 1, 1, 0}, new int[]{-683, -102, 0, -62, 0, 62, 102, 683}, 16, 21, 0);
    }
}
