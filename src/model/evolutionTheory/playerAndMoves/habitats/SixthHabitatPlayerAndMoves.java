package model.evolutionTheory.playerAndMoves.habitats;

import model.Board;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evolutionTheory.evaluationFunction.SuperEvaluationFunction;
import model.evolutionTheory.playerAndMoves.PlayerAndMovesIndividual;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.MoveType;
import model.player.timedPlayers.TimedTranspositionTablePlayer;

public class SixthHabitatPlayerAndMoves {
    //Individual{exploration=false, exploitation=false, mutationRate=0.1, genome=[[4], [3, 4, 0, 2, 1], [5, 8, 2, 6, 4, 1, 7, 3], [0], [13], [4], [2], [3],
    // [4], [6], [3], [0, 2, 5, 1, 3, 4], [1, 4, 3, 0, 2], [2, 0, 1], [1], [12], [7], [68], [18], [16]]}
    //MoveGeneratingStyle[] moveGeneratingStyles = {MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.ALL_TYPE_MOVES_DIRECTION_BY_DIRECTION, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION,MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_TYPE_BY_TYPE};
    //MoveType[] initialMoveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
    String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT";
    MoveGeneratingStyle moveGeneratingStyle = MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION;
    MoveType[] moveGeneratingMoveTypes = {MoveType.QUIET, MoveType.SACRIFICE,  MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_FAR, MoveType.FRIEND_ON_NEAR};
    int[] directions = new int[]{5, 8, 2, 6, 4, 1, 7, 3};
    boolean moveGeneratingFrontToBack = false;
    int window = 13;
    int windowMultiplier = 4;
    int interactiveDepthRatio = 2;
    int fullDepthMoveNumber = 3;
    float roundsFactor = 0.4f;
    float towersFactor = 0.6f;
    float distancesFactor = 0.3f;
    int[] killerSort = new int[]{0, 2, 5, 1, 3, 4};
    int[] moveComparatorMoveTypes = new int[] {1, 4, 3, 0, 2};
    int[] comp = new int[] {2, 0, 1};
    boolean moveComparatorFrontToBack = true;
    int peakMove = 12;
    int midGameMoves = 7;
    double earlyFactor = 0.68;
    double midFactor = 0.18;
    double endFactor = 0.16;
    Board board = new BitBoard(FenInitial);
    MoveGenerator moveGenerator = new MoveGeneratorEvolutionTheory(board, moveGeneratingStyle, moveGeneratingMoveTypes, directions, moveGeneratingFrontToBack);
    EvaluationFunction evaluationFunction = new SuperEvaluationFunction(board);

    public TimedTranspositionTablePlayer habitatPlayer(boolean isBlue, double totalTime) {
        return new TimedTranspositionTablePlayer(isBlue, board, moveGenerator,evaluationFunction, totalTime, window , windowMultiplier, interactiveDepthRatio,
                fullDepthMoveNumber, roundsFactor, towersFactor, distancesFactor,  killerSort, moveComparatorMoveTypes, comp, moveComparatorFrontToBack, peakMove,
                midGameMoves, earlyFactor, midFactor, endFactor);
    }

    //Individual{exploration=false, exploitation=false, mutationRate=0.1, genome=[[4], [3, 4, 0, 2, 1], [5, 8, 2, 6, 4, 1, 7, 3], [0], [13], [4], [2], [3], [4], [6], [3], [0, 2, 5, 1, 3, 4], [1, 4, 3, 0, 2], [2, 0, 1], [1], [12], [7], [68], [18], [16]]}
    public PlayerAndMovesIndividual habitatIndividual(){
        PlayerAndMovesIndividual habitatIndividual = new PlayerAndMovesIndividual();
        habitatIndividual.genome[0].value[0] = 4;
        habitatIndividual.genome[1].value = new int[] {3, 4, 0, 2, 1};
        habitatIndividual.genome[2].value = directions;
        habitatIndividual.genome[3].value[0] = 0;
        habitatIndividual.genome[4].value[0] = window;
        habitatIndividual.genome[5].value[0] = windowMultiplier;
        habitatIndividual.genome[6].value[0] = interactiveDepthRatio;
        habitatIndividual.genome[7].value[0] = fullDepthMoveNumber;
        habitatIndividual.genome[8].value[0] = (int) (roundsFactor * 10);
        habitatIndividual.genome[9].value[0] = (int) (towersFactor * 10);
        habitatIndividual.genome[10].value[0] = (int) (distancesFactor * 10);
        habitatIndividual.genome[11].value = killerSort;
        habitatIndividual.genome[12].value = moveComparatorMoveTypes;
        habitatIndividual.genome[13].value = comp;
        habitatIndividual.genome[14].value[0] = 1;
        habitatIndividual.genome[15].value[0] = peakMove;
        habitatIndividual.genome[16].value[0] = midGameMoves;
        habitatIndividual.genome[17].value[0] = (int) (earlyFactor * 100);
        habitatIndividual.genome[18].value[0] = (int) (midFactor * 100);
        habitatIndividual.genome[19].value[0] = (int) (endFactor * 100);
        return habitatIndividual;
    }

}
