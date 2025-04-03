package model.evolutionTheory.playerAndMoves;

import model.Board;
import model.Game;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evolutionTheory.evaluationFunction.SuperEvaluationFunction;
import model.evolutionTheory.individual.Individual;
import model.evolutionTheory.playerAndMoves.habitats.*;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.MoveType;
import model.player.Player;
import model.player.timedPlayers.TimedTranspositionTablePlayer;
import view.UserInput;

import java.io.*;
import java.util.*;

public class PlayerAndMovesWorld implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    Random random = new Random();
    List<Individual> pool = new ArrayList<>();
    List<List<Individual>> families;
    List<Individual> family;
    List<Individual> population = new ArrayList<>();
    List<Individual> populationNew = new ArrayList<>();
    String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT,b";
    MoveType[] initialMoveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
    MoveGeneratingStyle[] moveGeneratingStyles = {MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_PIECE_BY_PIECE, MoveGeneratingStyle.ALL_TYPE_MOVES_DIRECTION_BY_DIRECTION, MoveGeneratingStyle.TYPE_BY_TYPE_MOVES_DIRECTION_BY_DIRECTION,MoveGeneratingStyle.DIRECTION_BY_DIRECTION_MOVES_TYPE_BY_TYPE};
    int generation = 0;
    public Individual bestOfBest;
    public Individual finalBest;
    public Individual superBest;

    public List<int [][]> worldRank = new ArrayList<>();
    public List<int [][]> protoRank = new ArrayList<>();
    public List<int [][]> finalRank = new ArrayList<>();
    public List<int [][]> superRank = new ArrayList<>();

    public void savePlayerCheckpoint(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(pool);
            out.writeObject(families);
            out.writeObject(family);
            out.writeObject(population);
            out.writeObject(populationNew);
            out.writeObject(bestOfBest);
            out.writeInt(generation);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadPlayerCheckpoint(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.pool = (List<Individual>) in.readObject();
            playerAndMovesWorld.families = (List<List<Individual>>) in.readObject();
            playerAndMovesWorld.family = (List<Individual>) in.readObject();
            playerAndMovesWorld.population = (List<Individual>) in.readObject();
            playerAndMovesWorld.populationNew = (List<Individual>) in.readObject();
            playerAndMovesWorld.bestOfBest = (Individual)in.readObject();
            playerAndMovesWorld.generation = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void saveWorldRanking(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(worldRank);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadWorldRanking(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.worldRank = (List<int [][]>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void saveProtoRanking(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(protoRank);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadProtoRanking(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.protoRank = (List<int [][]>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void savePlayerFinalCheckpoint(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(pool);
            out.writeObject(families);
            out.writeObject(family);
            out.writeObject(population);
            out.writeObject(populationNew);
            out.writeObject(finalBest);
            out.writeInt(generation);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadPlayerFinalCheckpoint(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.pool = (List<Individual>) in.readObject();
            playerAndMovesWorld.families = (List<List<Individual>>) in.readObject();
            playerAndMovesWorld.family = (List<Individual>) in.readObject();
            playerAndMovesWorld.population = (List<Individual>) in.readObject();
            playerAndMovesWorld.populationNew = (List<Individual>) in.readObject();
            playerAndMovesWorld.finalBest = (Individual)in.readObject();
            playerAndMovesWorld.generation = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void saveFinalRanking(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(finalRank);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadFinalRanking(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.finalRank = (List<int [][]>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void savePlayerSuperCheckpoint(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(pool);
            out.writeObject(families);
            out.writeObject(family);
            out.writeObject(population);
            out.writeObject(populationNew);
            out.writeObject(superBest);
            out.writeInt(generation);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadPlayerSuperCheckpoint(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.pool = (List<Individual>) in.readObject();
            playerAndMovesWorld.families = (List<List<Individual>>) in.readObject();
            playerAndMovesWorld.family = (List<Individual>) in.readObject();
            playerAndMovesWorld.population = (List<Individual>) in.readObject();
            playerAndMovesWorld.populationNew = (List<Individual>) in.readObject();
            playerAndMovesWorld.superBest = (Individual)in.readObject();
            playerAndMovesWorld.generation = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void saveSuperRanking(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(superRank);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesWorld loadSuperRanking(String filename) {
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesWorld.superRank = (List<int [][]>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesWorld;
    }

    public void doMigration(){
        population.add(new FirstHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SecondHabitatPlayerAndMoves().habitatIndividual());
        population.add(new ThirdHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FourthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FifthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SixthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new EighthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new NinthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new TenthHabitatPlayerAndMoves().habitatIndividual());
        printPopulation();
    }

    public void worldRankPopulation(){
        population.add(new FirstHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SecondHabitatPlayerAndMoves().habitatIndividual());
        population.add(new ThirdHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FourthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FifthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SixthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new EighthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new NinthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new TenthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new InitialPlayerAndMoves().habitatIndividual());
        population.add(new WorldPlayerAndMoves().habitatIndividual());
        printPopulation();
    }

    public void protoRankPopulation(){
        population.add(new FirstHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SecondHabitatPlayerAndMoves().habitatIndividual());
        population.add(new ThirdHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FourthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FifthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SixthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new EighthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new NinthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new TenthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new InitialPlayerAndMoves().habitatIndividual());
        population.add(new FinalPlayerAndMoves().habitatIndividual());
        printPopulation();
    }

    public void topFourPopulation(){
        population.add(new SecondHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new TenthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new WorldPlayerAndMoves().habitatIndividual());
        printPopulation();
    }

    public void finalRankPopulation(){
        population.add(new SecondHabitatPlayerAndMoves().habitatIndividual());
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new TenthHabitatPlayerAndMoves().habitatIndividual());
        population.add(new WorldPlayerAndMoves().habitatIndividual());
        population.add(new FinalPlayerAndMoves().habitatIndividual());
        printPopulation();
    }

    public void topTwoPopulation(){
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FinalPlayerAndMoves().habitatIndividual());
        printPopulation();
    }

    public void superRankPopulation(){
        population.add(new SeventhHabitatPlayerAndMoves().habitatIndividual());
        population.add(new FinalPlayerAndMoves().habitatIndividual());
        // TODO: 4/2/2025 here!!
        printPopulation();
    }

    public void explore(){
        System.out.println("Explore Mode on..");
        for (Individual individual : population){
            individual.explore();
        }
        printPopulation();
    }

    public void exploit(){
        System.out.println("Exploit Mode on..");
        for (Individual individual : population){
            individual.exploit();
        }
        printPopulation();
    }

    public void normalMode(){
        System.out.println("Normal Mode on..");
        for (Individual individual : population){
            individual.normalMode();
        }
        printPopulation();
    }

    public int [][] compete(Individual first, Individual second, double totalTime){
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
        MoveGenerator blueGenerator2 = new MoveGeneratorEvolutionTheory(blueBoard2, moveGeneratingStyles[second.genome[0].value[0]],secondGeneratingTypes, second.genome[2].value, secondGeneratingFrontToBack);
        MoveGenerator redGenerator2 = new MoveGeneratorEvolutionTheory(redBoard2, moveGeneratingStyles[first.genome[0].value[0]],firstGeneratingTypes, first.genome[2].value, firstGeneratingFrontToBack);
        UserInput userInput2 = new UserInput();

        EvaluationFunction firstEvaluationFunction = new SuperEvaluationFunction(blueBoard);
        EvaluationFunction secondEvaluationFunction = new SuperEvaluationFunction(redBoard);
        EvaluationFunction thirdEvaluationFunction = new SuperEvaluationFunction(redBoard2);
        EvaluationFunction fourthEvaluationFunction = new SuperEvaluationFunction(blueBoard2);

        Player firstBlue = new TimedTranspositionTablePlayer(true, blueBoard,blueGenerator, firstEvaluationFunction,totalTime,first.genome[4].value[0],first.genome[5].value[0],first.genome[6].value[0],first.genome[7].value[0],
                (((float)(first.genome[8].value[0]))/10),(((float)(first.genome[9].value[0]))/10),(((float)(first.genome[10].value[0]))/10), first.genome[11].value, first.genome[12].value,first.genome[13].value,
                firstComparingFrontToBack, first.genome[15].value[0], first.genome[16].value[0],(((float)(first.genome[17].value[0]))/100),(((float)(first.genome[18].value[0]))/100),(((float)(first.genome[19].value[0]))/100));
        Player firstRed = new TimedTranspositionTablePlayer(false, redBoard,redGenerator, secondEvaluationFunction,totalTime,second.genome[4].value[0],second.genome[5].value[0],second.genome[6].value[0],second.genome[7].value[0],
                (((float)(second.genome[8].value[0]))/10),(((float)(second.genome[9].value[0]))/10),(((float)(second.genome[10].value[0]))/10), second.genome[11].value, second.genome[12].value,second.genome[13].value,
                secondComparingFrontToBack, second.genome[15].value[0], second.genome[16].value[0],(((float)(second.genome[17].value[0]))/100),(((float)(second.genome[18].value[0]))/100),(((float)(second.genome[19].value[0]))/100));

        Player secondBlue = new TimedTranspositionTablePlayer(true, blueBoard2,blueGenerator2, fourthEvaluationFunction, totalTime,second.genome[4].value[0],second.genome[5].value[0],second.genome[6].value[0],second.genome[7].value[0],
                (((float)(second.genome[8].value[0]))/10),(((float)(second.genome[9].value[0]))/10),(((float)(second.genome[10].value[0]))/10), second.genome[11].value, second.genome[12].value,second.genome[13].value,
                secondComparingFrontToBack, second.genome[15].value[0], second.genome[16].value[0],(((float)(second.genome[17].value[0]))/100),(((float)(second.genome[18].value[0]))/100),(((float)(second.genome[19].value[0]))/100));
        Player secondRed = new TimedTranspositionTablePlayer(false , redBoard2,redGenerator2, thirdEvaluationFunction, totalTime,first.genome[4].value[0],first.genome[5].value[0],first.genome[6].value[0],first.genome[7].value[0],
                (((float)(first.genome[8].value[0]))/10),(((float)(first.genome[9].value[0]))/10),(((float)(first.genome[10].value[0]))/10), first.genome[11].value, first.genome[12].value,first.genome[13].value,
                firstComparingFrontToBack, first.genome[15].value[0], first.genome[16].value[0],(((float)(first.genome[17].value[0]))/100),(((float)(first.genome[18].value[0]))/100),(((float)(first.genome[19].value[0]))/100));
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
    public void printPopulation(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        for (Individual individual : population){
            System.out.println(individual);
        }
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");

    }
    public int[] findTopIndices(int[][] arrays, int topCount) {
        if (topCount > arrays.length) {
            throw new IllegalArgumentException("topCount cannot be greater than the number of arrays.");
        }

        // Array of indices to sort and find the top results
        Integer[] indices = new Integer[arrays.length];
        for (int i = 0; i < arrays.length; i++) {
            indices[i] = i;
        }

        // Sort the indices based on the comparison logic
        Arrays.sort(indices, (a, b) -> {
            for (int i = 0; i < 4; i++) {
                if (i == 0) {
                    // For the first element, higher value is better
                    if (arrays[b][i] != arrays[a][i]) {
                        return arrays[b][i] - arrays[a][i];
                    }
                } else {
                    // For other elements, lower value is better
                    if (arrays[a][i] != arrays[b][i]) {
                        return arrays[a][i] - arrays[b][i];
                    }
                }
            }
            return 0; // All elements are equal
        });

        // Retrieve the top `topCount` indices
        int[] topIndices = new int[topCount];
        for (int i = 0; i < topCount; i++) {
            topIndices[i] = indices[i];
        }

        return topIndices;
    }

    public void printGenerationNumber(){
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++ " + generation + " +++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
    }

    public void rankPopulation(double totalTime, int elitismNum, int poolNum){
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int i = 0; i < (population.size() - 1); i++){
            for (int j = (i + 1); j < population.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Ranking:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(population.get(i), population.get(j), totalTime);
                for (int k = 0; k < 4; k++){
                    populationResults[i][k] = populationResults[i][k] + pairResults[0][k];
                    populationResults[j][k] = populationResults[j][k] + pairResults[1][k];
                }
            }
        }
        for (int i = 0; i < population.size(); i++){
            String s = "";
            for (int k = 0; k < 4; k++){
                s += populationResults[i][k] + " | ";
            }
            System.out.println(s);
        }
        printPopulation();
        populationNew = new ArrayList<>();
        int[]populationBest = findTopIndices(populationResults,elitismNum);
        int[]poolIndices = findTopIndices(populationResults,poolNum);
        for (int index : populationBest){
            populationNew.add(population.get(index));
        }
        for (int index : poolIndices){
            pool.add(population.get(index));
        }
        population = populationNew;
        populationNew = new ArrayList<>();
        System.out.println("////////////////////////");
    }

    public void rankWorld(double totalTime){
        worldRankPopulation();
        int [][] results = new int[population.size()][5];
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int i = 0; i < (population.size() - 1); i++){
            for (int j = (i + 1); j < population.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Ranking:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(population.get(i), population.get(j), totalTime);
                for (int k = 0; k < 4; k++){
                    populationResults[i][k] = populationResults[i][k] + pairResults[0][k];
                    populationResults[j][k] = populationResults[j][k] + pairResults[1][k];
                }
            }
        }
        int[]populationBest = findTopIndices(populationResults, population.size());
        for (int j = 0; j < populationBest.length; j++){
            results[populationBest[j]][0] = j + 1;
        }
        for (int i = 0; i < population.size(); i++){
            for (int k = 0; k < 4; k++){
                results [i][k + 1]= populationResults[i][k];
            }
        }
        for (int i = 0; i < population.size(); i++){
            String s = "";
            for (int k = 0; k < 5; k++){
                s += results [i][k] + " | ";
            }
            System.out.println(s);
        }
        worldRank.add(results);
        saveWorldRanking("PlayerAndMovesWorldRank.ser");
    }

    public void rankFinal(double totalTime){
        finalRankPopulation();
        int [][] results = new int[population.size()][5];
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int i = 0; i < (population.size() - 1); i++){
            for (int j = (i + 1); j < population.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Ranking:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(population.get(i), population.get(j), totalTime);
                for (int k = 0; k < 4; k++){
                    populationResults[i][k] = populationResults[i][k] + pairResults[0][k];
                    populationResults[j][k] = populationResults[j][k] + pairResults[1][k];
                }
            }
        }
        int[]populationBest = findTopIndices(populationResults, population.size());
        for (int j = 0; j < populationBest.length; j++){
            results[populationBest[j]][0] = j + 1;
        }
        for (int i = 0; i < population.size(); i++){
            for (int k = 0; k < 4; k++){
                results [i][k + 1]= populationResults[i][k];
            }
        }
        for (int i = 0; i < population.size(); i++){
            String s = "";
            for (int k = 0; k < 5; k++){
                s += results [i][k] + " | ";
            }
            System.out.println(s);
        }
        finalRank.add(results);
        saveFinalRanking("PlayerAndMovesFinalRank.ser");
    }

    public void rankSuper(double totalTime){
        superRankPopulation();
        int [][] results = new int[population.size()][5];
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int i = 0; i < (population.size() - 1); i++){
            for (int j = (i + 1); j < population.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Ranking:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(population.get(i), population.get(j), totalTime);
                for (int k = 0; k < 4; k++){
                    populationResults[i][k] = populationResults[i][k] + pairResults[0][k];
                    populationResults[j][k] = populationResults[j][k] + pairResults[1][k];
                }
            }
        }
        int[]populationBest = findTopIndices(populationResults, population.size());
        for (int j = 0; j < populationBest.length; j++){
            results[populationBest[j]][0] = j + 1;
        }
        for (int i = 0; i < population.size(); i++){
            for (int k = 0; k < 4; k++){
                results [i][k + 1]= populationResults[i][k];
            }
        }
        for (int i = 0; i < population.size(); i++){
            String s = "";
            for (int k = 0; k < 5; k++){
                s += results [i][k] + " | ";
            }
            System.out.println(s);
        }
        superRank.add(results);
        saveSuperRanking("PlayerAndMovesSuperRank.ser");
    }

    public void rankProto(double totalTime){
        protoRankPopulation();
        int [][] results = new int[population.size()][5];
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int j = 0; j < (population.size() - 1); j++){
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("Ranking:{ game: proto X " + (j+1) + " }");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
            pairResults = compete(population.get(population.size() - 1), population.get(j), totalTime);
            for (int k = 0; k < 4; k++){
                populationResults[population.size() - 1][k] = populationResults[population.size() - 1][k] + pairResults[0][k];
                populationResults[j][k] = populationResults[j][k] + pairResults[1][k];
            }
        }
        int[]populationBest = findTopIndices(populationResults, population.size());
        for (int j = 0; j < populationBest.length; j++){
            results[populationBest[j]][0] = j + 1;
        }
        for (int i = 0; i < population.size(); i++){
            for (int k = 0; k < 4; k++){
                results [i][k + 1]= populationResults[i][k];
            }
        }
        for (int i = 0; i < population.size(); i++){
            String s = "";
            for (int k = 0; k < 5; k++){
                s += results [i][k] + " | ";
            }
            System.out.println(s);
        }
        protoRank.add(results);
        saveProtoRanking("ProtoFinalPlayerAndMovesRank.ser");
    }

    public void printRanking(List<int [][]> rankingList){
        for (int [][] rank: rankingList){
            for (int i = 0; i < rank.length; i++){
                String s = "";
                for (int k = 0; k < 5; k++){
                    s += rank [i][k] + " | ";
                }
                System.out.println(s);
            }
        }
    }

    public void firstScarcitySeason(double totalTime){
        pool.addAll(population);
        families = new ArrayList<>();
        population = new ArrayList<>();
        Individual[] offspring;
        System.out.println("Mating started..");
        Collections.shuffle(pool, random);
        for (int i = 0; i < pool.size() - 1; i += 2) {
            System.out.println("-------------------------------");
            System.out.println("pair: " + i + " and " + (i + 1));
            Individual firstParent = pool.get(i);
            Individual secondParent = pool.get(i + 1);
            System.out.println("parents were chosen..");
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println(firstParent);
            System.out.println(secondParent);
            System.out.println("+++++++++++ Parents +++++++++++");
            family = new ArrayList<>();
            family.add(firstParent);
            family.add(secondParent);
            System.out.println("applying crossover..");
            offspring = firstParent.crossover(secondParent);
            if (offspring != null && offspring.length > 0) {
                family.addAll(Arrays.asList(offspring));
                families.add(family);
            } else {
                System.out.println("Crossover failed for pair: " + i + " and " + (i + 1));
                while (true){
                    offspring = firstParent.crossover(secondParent);
                    if (offspring != null && offspring.length > 0) {
                        family.addAll(Arrays.asList(offspring));
                        families.add(family);
                        System.out.println("Problem fixed!");
                        break;
                    }
                    System.out.println("Crossover failed for pair: " + i + " and " + (i + 1));
                }
            }
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println(firstParent);
            System.out.println(secondParent);
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println("-------------------------------");
        }
        pool = new ArrayList<>();
        System.out.println("Mating done..");
        fullFamilySelection(totalTime);
        System.out.println("-------------firstScarcitySeason -------------");
        printPopulation();
        System.out.println("-------------firstScarcitySeason -------------");
    }

    public void fullFamilySelection(double totalTime){
        int [][] familyResults;
        int [][] pairResults;
        for (int i = 0; i < families.size(); i++){
            family = families.get(i);
            familyResults = new int[6][4];
            for (int j = 0; j < (family.size() - 1); j++){
                for (int k = (j + 1) ; k < family.size(); k++){
                    System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                    System.out.println("Family number " + (i+1) + ":{ game: " + (j+1) + " X " + (k+1) + " }");
                    System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
                    pairResults = compete(family.get(j), family.get(k),totalTime);
                    for (int l = 0; l < 4; l++){
                        familyResults[j][l] = familyResults[j][l] + pairResults[0][l];
                        familyResults[k][l] = familyResults[k][l] + pairResults[1][l];
                    }
                }
            }
            int[]familyBest = findTopIndices(familyResults,2);
            for (int index : familyBest){
                System.out.println(index);
                population.add(family.get(index));

            }
            System.out.println("---------------------");
            System.out.println("---------------------");
            System.out.println("---------------------");
            for (int m = 0 ; m < 6; m++){
                String s = "";
                for (int n = 0 ; n < 4; n++){
                    s += familyResults[m][n] + " | ";
                }
                System.out.println(s);
            }
            System.out.println("////////////////////////");
        }
    }
    public void famine(){
        printPopulation();
        for (int i = this.generation; i < 5; i++){
            printGenerationNumber();
            if (i == 1) explore();
            else if (i == 2) normalMode();
            firstScarcitySeason(120000);
            generation++;
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        if (generation == 5){
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 8 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            rankPopulation(120000,8,0);
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
        }
        for (int i = this.generation; i < 10; i++){
            printGenerationNumber();
            if (i == 6) explore();
            else if (i == 7) normalMode();
            firstScarcitySeason(120000);
            generation++;
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        if (this.generation == 10 && this.population.size() > 6){
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 6 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            rankPopulation(120000,6,0);
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
        }
        for (int i = this.generation; i < 15; i++){
            printGenerationNumber();
            if (i == 11) explore();
            else if (i == 12) normalMode();
            firstScarcitySeason(120000);
            generation++;
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        if (this.generation == 15 && this.population.size() > 4){
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 4 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            rankPopulation(120000,4,0);
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
        }
        for (int i = this.generation; i < 20; i++){
            printGenerationNumber();
            if (i == 16) exploit();
            else if (i == 12) normalMode();
            firstScarcitySeason(120000);
            generation++;
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        if (this.generation == 20 && this.population.size() > 2){
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 2 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            rankPopulation(120000,2,0);
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
        }
        for (int i = this.generation; i < 40; i++){
            printGenerationNumber();
            if (i == 21 || i == 26 || i == 28 || (i > 32 && i < 35) || i > 38) exploit();
            else if (i == 23 || i == 30 || i == 32 || i == 35) normalMode();
            else if(i == 31)explore();
            firstScarcitySeason(120000);
            generation++;
            savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        printGenerationNumber();
        rankPopulation(120000,1,0);
        generation++;
        savePlayerCheckpoint("world_checkpoint_" + generation + ".ser");
        printPopulation();
        printGenerationNumber();
        normalMode();
        bestOfBest = population.get(0);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("000000000000000  BEST OF BEST  0000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println(bestOfBest);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("000000000000000  BEST OF BEST  0000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
    }

    public void fullOptimization(){
        if (this.generation < 1)doMigration();
        famine();
    }

    public void finalFour(){
        if (this.generation < 1) topFourPopulation();

        for (int i = this.generation; i < 6; i++){
            printGenerationNumber();
            if (i == 1 ||i == 2) explore();
            else if (i == 3 || i == 5) normalMode();
            else if (i == 4)exploit();
            firstScarcitySeason(120000);
            generation++;
            savePlayerFinalCheckpoint("PlayerFinal_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        if (this.generation > 5 && this.population.size() > 2){
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 2 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            rankPopulation(120000,2,0);
            savePlayerFinalCheckpoint("PlayerFinal_checkpoint_" + generation + ".ser");
        }
        for (int i = this.generation; i < 15; i++){
            printGenerationNumber();
            if (i == 7) explore();
            else if (i == 8 || i == 11) normalMode();
            else if (i == 9 || i > 12)exploit();
            firstScarcitySeason(120000);
            generation++;
            savePlayerFinalCheckpoint("PlayerFinal_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        printGenerationNumber();
        rankPopulation(120000,1,0);
        generation++;
        printPopulation();
        printGenerationNumber();
        normalMode();
        finalBest = population.get(0);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("000000000000000  BEST OF Final 0000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println(finalBest);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("000000000000000  BEST OF Final 0000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        savePlayerFinalCheckpoint("PlayerFinal_checkpoint_" + generation + ".ser");

    }

    public void finalTwo(){
        if (this.generation < 1) topTwoPopulation();
        for (int i = this.generation; i < 15; i++){
            printGenerationNumber();
            if (i == 1 ||i == 2 || i == 6) explore();
            else if (i == 3 || i == 5 || i == 7 || i == 11) normalMode();
            else if (i == 4 || i == 9 || i > 12)exploit();
            firstScarcitySeason(120000);
            generation++;
            savePlayerSuperCheckpoint("PlayerSuper_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        printGenerationNumber();
        rankPopulation(120000,1,0);
        generation++;
        printPopulation();
        printGenerationNumber();
        normalMode();
        finalBest = population.get(0);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("0000000000000000  SUPER BEST 000000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println(superBest);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("0000000000000000  SUPER BEST 000000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        savePlayerSuperCheckpoint("PlayerSuper_checkpoint_" + generation + ".ser");
    }
    public static void main (String[] args){
        PlayerAndMovesWorld playerAndMovesWorld = new PlayerAndMovesWorld();
        playerAndMovesWorld.finalTwo();

        //playerAndMovesWorld.rankFinal(120000);
        //PlayerAndMovesWorld playerAndMovesWorld = loadPlayerFinalCheckpoint("PlayerFinal_checkpoint_13.ser");
        //playerAndMovesWorld.finalFour();

        //PlayerAndMovesWorld playerAndMovesWorld = loadPlayerCheckpoint("world_checkpoint_35.ser");
        //playerAndMovesWorld.fullOptimization();
        //PlayerAndMovesWorld playerAndMovesWorld = loadWorldRanking("PlayerAndMovesWorldRank.ser");
        //playerAndMovesWorld.printRanking(playerAndMovesWorld.worldRank);
/*
        playerAndMovesWorld.rankProto(120000);
        System.out.println("++++++ +++++ ++++++ ++++++ ++++++");
        playerAndMovesWorld.printRanking(playerAndMovesWorld.protoRank);
*/

    }
}
