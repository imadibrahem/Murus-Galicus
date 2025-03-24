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

public class TenthHabitatPlayerAndMoves {
    //Individual{exploration=false, exploitation=false, mutationRate=0.1, genome=[[2], [2, 4, 1, 0, 3], [6, 7, 1, 8, 3, 5, 4, 2], [0], [23], [6], [2], [11],
    // [4], [-6], [-6], [0, 2, 3, 4, 5, 1], [2, 4, 3, 0, 1], [1, 0, 2], [1], [10], [5], [66], [17], [20]]}
    //MoveGeneratingStyle[] moveGeneratingStyles = {MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.ALL_TYPE_MOVES_DIRECTION_BY_DIRECTION, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION,MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_TYPE_BY_TYPE};
    //MoveType[] initialMoveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
    String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT";
    MoveGeneratingStyle moveGeneratingStyle = MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE;
    MoveType[] moveGeneratingMoveTypes = {MoveType.FRIEND_ON_FAR, MoveType.SACRIFICE, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_BOTH, MoveType.QUIET};
    int[] directions = new int[]{6, 7, 1, 8, 3, 5, 4, 2};
    boolean moveGeneratingFrontToBack = false;
    int window = 23;
    int windowMultiplier = 6;
    int interactiveDepthRatio = 2;
    int fullDepthMoveNumber = 11;
    float roundsFactor = 0.4f;
    float towersFactor = -0.6f;
    float distancesFactor = -0.6f;
    int[] killerSort = new int[]{0, 2, 3, 4, 5, 1};
    int[] moveComparatorMoveTypes = new int[] {2, 4, 3, 0, 1};
    int[] comp = new int[] {1, 0, 2};
    boolean moveComparatorFrontToBack = true;
    int peakMove = 10;
    int midGameMoves = 5;
    double earlyFactor = 0.66;
    double midFactor = 0.17;
    double endFactor = 0.20;
    Board board = new BitBoard(FenInitial);
    MoveGenerator moveGenerator = new MoveGeneratorEvolutionTheory(board, moveGeneratingStyle, moveGeneratingMoveTypes, directions, moveGeneratingFrontToBack);
    EvaluationFunction evaluationFunction = new SuperEvaluationFunction(board);

    public TimedTranspositionTablePlayer habitatPlayer(boolean isBlue, double totalTime) {
        return new TimedTranspositionTablePlayer(isBlue, board, moveGenerator,evaluationFunction, totalTime, window , windowMultiplier, interactiveDepthRatio,
                fullDepthMoveNumber, roundsFactor, towersFactor, distancesFactor,  killerSort, moveComparatorMoveTypes, comp, moveComparatorFrontToBack, peakMove,
                midGameMoves, earlyFactor, midFactor, endFactor);
    }

    //Individual{exploration=false, exploitation=false, mutationRate=0.1, genome=[[2], [2, 4, 1, 0, 3], [6, 7, 1, 8, 3, 5, 4, 2], [0], [23], [6], [2], [11], [4], [-6], [-6], [0, 2, 3, 4, 5, 1], [2, 4, 3, 0, 1], [1, 0, 2], [1], [10], [5], [66], [17], [20]]}
    public PlayerAndMovesIndividual habitatIndividual(){
        PlayerAndMovesIndividual habitatIndividual = new PlayerAndMovesIndividual();
        habitatIndividual.genome[0].value[0] = 2;
        habitatIndividual.genome[1].value = new int[] {2, 4, 1, 0, 3};
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
