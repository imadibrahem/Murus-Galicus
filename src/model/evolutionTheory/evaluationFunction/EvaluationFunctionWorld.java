package model.evolutionTheory.evaluationFunction;

import model.Board;
import model.Game;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evaluationFunction.InitialEvaluationFunction;
import model.evolutionTheory.OptimumEvaluationFunction;
import model.evolutionTheory.habitats.*;
import model.evolutionTheory.individual.Individual;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.MoveType;
import model.player.Player;
import model.player.TranspositionTablePlayer;
import view.UserInput;

import java.io.*;
import java.util.*;

public class EvaluationFunctionWorld implements Serializable {
    @Serial
    protected static final long serialVersionUID = 1L;
    Random random = new Random();
    List<Individual> pool = new ArrayList<>();
    List<List<Individual>> families;
    List<Individual> family;
    List<Individual> population = new ArrayList<>();
    List<Individual> populationNew = new ArrayList<>();
    String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT,b";
    MoveType[] moveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
    int [] directions = {1, 8, 2, 3, 7, 6, 4, 5};
    int generation = 0;
    public Individual bestOfBest;
    public List<int [][]> worldRank = new ArrayList<>();

    public void doMigration(){
        population.add(new EvaluationFunctionIndividual(new FirstHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new SecondHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new ThirdHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new FourthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new FifthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new SixthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new SeventhHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new EighthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new NinthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new TenthHabitatEvaluationFunction()));
        printPopulation();
    }
    public void worldRankPopulation(){
        population.add(new EvaluationFunctionIndividual(new FirstHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new SecondHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new ThirdHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new FourthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new FifthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new SixthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new SeventhHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new EighthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new NinthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new TenthHabitatEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new InitialEvaluationFunction()));
        population.add(new EvaluationFunctionIndividual(new OptimumEvaluationFunction()));
        printPopulation();

    }


    public void saveCheckpoint(String filename) {
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

    public static EvaluationFunctionWorld loadCheckpoint(String filename) {
        EvaluationFunctionWorld evaluationFunctionWorld = new EvaluationFunctionWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            evaluationFunctionWorld.pool = (List<Individual>) in.readObject();
            evaluationFunctionWorld.families = (List<List<Individual>>) in.readObject();
            evaluationFunctionWorld.family = (List<Individual>) in.readObject();
            evaluationFunctionWorld.population = (List<Individual>) in.readObject();
            evaluationFunctionWorld.populationNew = (List<Individual>) in.readObject();
            evaluationFunctionWorld.bestOfBest = (Individual)in.readObject();
            evaluationFunctionWorld.generation = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return evaluationFunctionWorld;
    }

    public void saveRanking(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(worldRank);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EvaluationFunctionWorld loadRanking(String filename) {
        EvaluationFunctionWorld evaluationFunctionWorld = new EvaluationFunctionWorld();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            evaluationFunctionWorld.worldRank = (List<int [][]>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return evaluationFunctionWorld;
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


    public void printFamilies(){
        for (int i = 0; i < families.size(); i++){
            System.out.println(families.get(i));
            System.out.println("Family number " + (i+1) + ":{");
            for (int j = 0; j < families.get(i).size(); j++){
                if (j < 2) System.out.println("Parent number " + (j+1)+":");
                else System.out.println("Child number " + (j-1)+":");
                System.out.println(families.get(i).get(j));
            }
            System.out.println("}");
        }
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

    public void printGenerationNumber(){
        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++++++++++++ " + generation + " +++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();
    }

    public int [][] compete(Individual first, Individual second, int depth){
        int [] firstReports = new int[4];
        int [] secondReports = new int[4];
        int [][] result = new int[2][4];
        Board blueBoard = new BitBoard(Game.FenTrimmer(FenInitial));
        Board redBoard = new BitBoard(Game.FenTrimmer(FenInitial));
        MoveGenerator blueGenerator = new MoveGeneratorEvolutionTheory(blueBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        MoveGenerator redGenerator = new MoveGeneratorEvolutionTheory(redBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
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

    public void rankPopulation(int depth, int elitismNum, int poolNum){
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int i = 0; i < (population.size() - 1); i++){
            for (int j = (i + 1); j < population.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Ranking:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(population.get(i), population.get(j), depth);
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
        populationNew = new ArrayList<>();
        int[]populationBest = findTopIndices(populationResults,elitismNum);
        int[]poolIndices = findTopIndices(populationResults,poolNum);
        printPopulation();

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

    public int [][] worldDepthRanking(int depth){
        int [][] results = new int[population.size()][5];
        int [][] pairResults;
        int [][] populationResults= new int[population.size()][4];
        for (int i = 0; i < (population.size() - 1); i++){
            for (int j = (i + 1); j < population.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("Ranking:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(population.get(i), population.get(j), depth);
                for (int k = 0; k < 4; k++){
                    populationResults[i][k] = populationResults[i][k] + pairResults[0][k];
                    populationResults[j][k] = populationResults[j][k] + pairResults[1][k];
                }
            }
        }
        int[]populationBest = findTopIndices(populationResults,population.size());
        for (int i = 0; i < population.size(); i++){
            for (int j = 0; j < populationBest.length; j++){
                results[populationBest[j]][0] = j + 1;
            }
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
        System.out.println("////////////////////////");
        return results;
    }

    public void worldRanking(int startDepth, int endDepth){
        worldRankPopulation();
        for (int i = startDepth; i < (endDepth + 1); i++){
            System.out.println();
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||| Ranking for depth " + i + " ||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
            int[][] rankDepth = worldDepthRanking(i);
            worldRank.add(rankDepth);
            saveRanking("WorldRank.ser");
        }
    }

    public void printWorldRank(){
        String sumString = "";
        for (int i = 0; i < worldRank.size(); i++){
            System.out.println("Depth Nr.("+ (i+2) + "): " + (Arrays.deepToString(worldRank.get(i))));
        }
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

        for (int j = 0; j < 12; j++){
            sumString += "Fun Nr.(" + (j+1) + "): [";
            int []sumFunction = new int [5];
            for (int i = 0; i < worldRank.size(); i++){
                for (int k = 0; k < 5; k++){
                    sumFunction[k] += worldRank.get(i)[j][k];
                }

            }
            for (int k = 0; k < 5; k++){
                sumString += (sumFunction[k]+ " | ");
            }
            sumString +="]\n";
        }
        System.out.println(sumString);
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
    }

    public void firstScarcitySeason(int depth){
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
        fullFamilySelection(depth);
        System.out.println("-------------firstScarcitySeason -------------");
        printPopulation();
        System.out.println("-------------firstScarcitySeason -------------");
    }

    public void fullFamilySelection(int depth){
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
                    pairResults = compete(family.get(j), family.get(k),depth);
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
        for (int i = this.generation; i < 20; i++){
            printGenerationNumber();
            if (i == 3) explore();
            else if (i == 8 || i == 18) normalMode();
            else if (i == 16)exploit();
            firstScarcitySeason(4);
            generation++;
            saveCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }
        if (this.generation < 21){
            System.out.println();
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 8 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
            rankPopulation(5,8,0);
        }

        for (int i = this.generation; i < 40; i++){
            printGenerationNumber();
            if (i == 24) explore();
            else if (i == 28 || i == 38) normalMode();
            else if (i == 36)exploit();
            firstScarcitySeason(5);
            generation++;
            saveCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }

        if (this.generation < 41){
            System.out.println();
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 6 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
            rankPopulation(6,6,0);
        }

        for (int i = this.generation; i < 50; i++){
            printGenerationNumber();
            if (i == 44) explore();
            else if (i == 45) normalMode();
            firstScarcitySeason(6);
            generation++;
            saveCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }

        if (this.generation < 51) {
            System.out.println();
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 4 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
            rankPopulation(6,4,0);
        }

        for (int i = this.generation; i < 56; i++){
            printGenerationNumber();
            if (i == 53)exploit();
            else if (i == 54) normalMode();
            firstScarcitySeason(6);
            generation++;
            saveCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }

        if (this.generation < 57){
            System.out.println();
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("||||||||||||||||||| Survival of the fittest 2 |||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
            System.out.println();
            rankPopulation(7,2,0);
        }

        for (int i = this.generation; i < 60; i++){
            printGenerationNumber();
            if (i == 58 || i == 59)exploit();
            firstScarcitySeason(7);
            generation++;
            saveCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }

        for (int i = this.generation; i < 62; i++){
            printGenerationNumber();
            exploit();
            firstScarcitySeason(8);
            generation++;
            saveCheckpoint("world_checkpoint_" + generation + ".ser");
            printPopulation();
        }


        printGenerationNumber();
        rankPopulation(9,1,0);
        generation++;
        saveCheckpoint("world_checkpoint_" + generation + ".ser");
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

    public static void main (String[] args){
        //EvaluationFunctionWorld evaluationFunctionWorld = new EvaluationFunctionWorld();
        EvaluationFunctionWorld evaluationFunctionWorld = loadRanking("WorldRank.ser");
        //EvaluationFunctionWorld evaluationFunctionWorld = loadCheckpoint("world_checkpoint_49.ser");
        //evaluationFunctionWorld.fullOptimization();
        //evaluationFunctionWorld.worldRanking(2, 6);
        evaluationFunctionWorld.printWorldRank();
    }

}
