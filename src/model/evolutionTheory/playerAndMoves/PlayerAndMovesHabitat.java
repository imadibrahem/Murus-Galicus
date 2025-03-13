package model.evolutionTheory.playerAndMoves;

import model.Board;
import model.Game;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evolutionTheory.evaluationFunction.EvolutionTheoryEvaluationFunction;
import model.evolutionTheory.evaluationFunction.individual.Individual;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.MoveType;
import model.player.Player;
import model.player.TranspositionTablePlayer;
import view.UserInput;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerAndMovesHabitat implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    Random random = new Random();
    List<Individual> pool = new ArrayList<>();
    List<List<Individual>> families;
    List<Individual> family;
    List<Individual> children;
    List<Individual> population = new ArrayList<>();
    List<Individual> populationNew = new ArrayList<>();
    String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT,b";
    MoveType[] initialMoveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
    MoveGeneratingStyle[] moveGeneratingStyles = {MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.ALL_TYPE_MOVES_DIRECTION_BY_DIRECTION, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION,MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_TYPE_BY_TYPE};
    int generation = 0;
    EvaluationFunction evaluationFunction;
    public Individual best;

    public void initiatePopulation(){
        for (int i = 0; i < 10; i++) population.add(new PlayerAndMovesIndividual());
    }
   /*
    public int [][] compete(Individual first, Individual second, int depth){

        int [] firstReports = new int[4];
        int [] secondReports = new int[4];
        int [][] result = new int[2][4];

        MoveType[] firstGeneratingTypes = {initialMoveTypes[first.genome[1].value[0]], initialMoveTypes[first.genome[1].value[1]], initialMoveTypes[first.genome[1].value[2]], initialMoveTypes[first.genome[1].value[3]], initialMoveTypes[first.genome[1].value[4]]};
        boolean firstGeneratingFrontToBack = (first.genome[3].value[0] == 1);
        boolean firstComparingFrontToBack = (first.genome[14].value[0] == 1);

        MoveType[] secondGeneratingTypes = {initialMoveTypes[second.genome[1].value[0]], initialMoveTypes[second.genome[1].value[1]], initialMoveTypes[second.genome[1].value[2]], initialMoveTypes[second.genome[1].value[3]], initialMoveTypes[second.genome[1].value[4]]};
        boolean secondGeneratingFrontToBack = (second.genome[3].value[0] == 1);
        boolean secondComparingFrontToBack = (second.genome[14].value[0] == 1);

        Board blueBoard = new BitBoard(Game.FenTrimmer(FenInitial));
        Board redBoard = new BitBoard(Game.FenTrimmer(FenInitial));
        MoveGenerator blueGenerator = new MoveGeneratorEvolutionTheory(blueBoard, moveGeneratingStyles[first.genome[0].value[0]],firstGeneratingTypes, first.genome[2].value, firstGeneratingFrontToBack);
        MoveGenerator redGenerator = new MoveGeneratorEvolutionTheory(redBoard, moveGeneratingStyles[second.genome[0].value[0]],secondGeneratingTypes, second.genome[2].value, secondGeneratingFrontToBack);
        UserInput userInput = new UserInput();

        Board blueBoard2 = new BitBoard(Game.FenTrimmer(FenInitial));
        Board redBoard2 = new BitBoard(Game.FenTrimmer(FenInitial));
        MoveGenerator blueGenerator2 = new MoveGeneratorEvolutionTheory(blueBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        MoveGenerator redGenerator2 = new MoveGeneratorEvolutionTheory(redBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        UserInput userInput2 = new UserInput();

        EvaluationFunction firstEvaluationFunction = new EvolutionTheoryEvaluationFunction(blueBoard,first);
        EvaluationFunction secondEvaluationFunction = new EvolutionTheoryEvaluationFunction(redBoard,second);
        EvaluationFunction thirdEvaluationFunction = new EvolutionTheoryEvaluationFunction(redBoard2,first);
        EvaluationFunction fourthEvaluationFunction = new EvolutionTheoryEvaluationFunction(blueBoard2,second);

        Player firstBlue = new TranspositionTablePlayer(true, blueBoard,blueGenerator, firstEvaluationFunction, depth);
        Player firstRed = new TranspositionTablePlayer(false, redBoard,redGenerator, secondEvaluationFunction,depth);
        Player secondBlue = new TranspositionTablePlayer(true, blueBoard2,blueGenerator2, fourthEvaluationFunction, depth);
        Player secondRed = new TranspositionTablePlayer(false , redBoard2,redGenerator2, thirdEvaluationFunction, depth);
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("First Game");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        Game one = new Game(userInput, firstRed, firstBlue, FenInitial);
        one.playGame();
        if (one.getWinner().equals(firstBlue)){
            System.out.println("Blue (first)");
            firstReports[0] += 1;
            for (int i = 0; i < one.getWinnerReport().length; i++){
                firstReports[i+1] = one.getWinnerReport()[i];
            }
        }
        else if (one.getWinner().equals(firstRed)){
            System.out.println("Red (first)");
            secondReports[0] += 1;
            for (int i = 0; i < one.getWinnerReport().length; i++){
                secondReports[i+1] = one.getWinnerReport()[i];
            }
        }
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("Second Game");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        Game two = new Game(userInput2, secondRed, secondBlue, FenInitial);
        two.playGame();
        if (two.getWinner().equals(secondBlue)){
            System.out.println("Blue (second)");
            secondReports[0] += 1;
            for (int i = 0; i < one.getWinnerReport().length; i++){
                secondReports[i+1] = one.getWinnerReport()[i];
            }
        }
        else if (two.getWinner().equals(secondRed)){
            System.out.println("Red (second)");
            firstReports[0] += 1;
            for (int i = 0; i < one.getWinnerReport().length; i++){
                firstReports[i+1] = one.getWinnerReport()[i];
            }
        }
        result[0] = firstReports;
        result[1] = secondReports;
        return result;


    }

    */



}
