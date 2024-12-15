package model.evaluationFunction;

import model.Board;

public class InitialEvaluationFunction extends EvaluationFunction{
    /*
    final int[] wallsDistancesFactor ={1, 2, 3, 4, 6, 4, 0};
    final int[] wallsColumnsFactor = {0, 1, 2, 3};
    final int[] towersDistancesFactor = {2, 4, 6, 8, 12, 8, 0};
    final int[] towersColumnsFactor = {0, 1, 4, 6};
    final int[] towersRatioFactor = {1, 2, 3, 3, 3, 4, 6};
    final int[] gameStateFactor =  {-50, -30, -20, -10, 10, 20, 30, 50};
    final int mobilityFactor = 2;
    final int isolatedTowersFactor = 2;
    final int isolatedWallsFactor = 5;

     */

    public InitialEvaluationFunction(Board board) {
        super(board, new int[]{1, 2, 3, 4, 6, 4, 200}, new int[]{0, 1, 2, 3}, new int[]{2, 4, 6, 8, 12, 8, 400},
                new int[]{0, 1, 4, 6}, new int[]{1, 2, 3, 3, 3, 4, 6, 7}, new int[]{-50, -30, 0, -20, 0, 20, 30, 50},
                6, 4, 10);
    }
}
