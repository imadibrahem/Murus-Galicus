package model.evolutionTheory.playerAndMoves;

import model.Board;
import model.Game;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evolutionTheory.evaluationFunction.FinalEvaluationFunction;
import model.evolutionTheory.evaluationFunction.SuperEvaluationFunction;
import model.evolutionTheory.evaluationFunction.individual.Individual;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.MoveType;
import model.player.Player;
import model.player.timedPlayers.TimedTranspositionTablePlayer;
import view.UserInput;

import java.io.*;
import java.util.*;

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
    public Individual best;

    public void savePlayerCheckpoint(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(pool);
            out.writeObject(families);
            out.writeObject(family);
            out.writeObject(children);
            out.writeObject(population);
            out.writeObject(populationNew);
            out.writeObject(best);
            out.writeInt(generation);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerAndMovesHabitat loadPlayerCheckpoint(String filename) {
        PlayerAndMovesHabitat playerAndMovesHabitat = new PlayerAndMovesHabitat();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            playerAndMovesHabitat.pool = (List<Individual>) in.readObject();
            playerAndMovesHabitat.families = (List<List<Individual>>) in.readObject();
            playerAndMovesHabitat.family = (List<Individual>) in.readObject();
            playerAndMovesHabitat.children = (List<Individual>) in.readObject();
            playerAndMovesHabitat.population = (List<Individual>) in.readObject();
            playerAndMovesHabitat.populationNew = (List<Individual>) in.readObject();
            playerAndMovesHabitat.best = (Individual)in.readObject();
            playerAndMovesHabitat.generation = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerAndMovesHabitat;
    }
    public void initiatePopulation(){
        for (int i = 0; i < 10; i++) population.add(new PlayerAndMovesIndividual());
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

    public void secondScarcitySeason(double totalTime){
        families = new ArrayList<>();
        population = new ArrayList<>();
        int pair = 0;
        System.out.println("Mating and Selection started..");
        Collections.shuffle(pool, random);
        Individual[] offspring;
        for (int h = 0; h < pool.size() - 1; h += 2) {
            System.out.println("-------------------------------");
            System.out.println("pair: " + h + " and " + (h + 1));
            pair++;
            family = new ArrayList<>();
            children = new ArrayList<>();
            Individual firstParent = pool.get(h);
            Individual secondParent = pool.get(h + 1);
            System.out.println("parents were chosen..");
            family.add(firstParent);
            family.add(secondParent);
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println(firstParent);
            System.out.println(secondParent);
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println("applying crossover..");
            offspring = firstParent.crossover(secondParent);
            if (offspring != null && offspring.length > 0) {
                children.addAll(Arrays.asList(offspring));

            } else {
                System.out.println("Crossover failed for pair: " + h + " and " + (h + 1));
                while (true){
                    offspring = firstParent.crossover(secondParent);
                    if (offspring != null && offspring.length > 0) {
                        children.addAll(Arrays.asList(offspring));
                        System.out.println("Problem fixed!");
                        break;
                    }
                    System.out.println("Crossover failed for pair: " + h + " and " + (h + 1));
                }
            }
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println(firstParent);
            System.out.println(secondParent);
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println("-------------------------------");
            int [][] pairResults;
            int [][] childrenResults= new int[children.size()][4];
            for (int i = 0; i < (children.size() - 1); i++){
                for (int j = (i + 1); j < children.size(); j++){
                    System.out.println("|||||||||||pair: "+ pair+" |||||||||||||||||||||||||||||||");
                    System.out.println("Children:{ game: " + (i+1) + " X " + (j+1) + " }");
                    System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                    pairResults = compete(children.get(i), children.get(j), totalTime);
                    for (int k = 0; k < 4; k++){
                        childrenResults[i][k] = childrenResults[i][k] + pairResults[0][k];
                        childrenResults[j][k] = childrenResults[j][k] + pairResults[1][k];
                    }
                }
            }
            int[]childrenBest = findTopIndices(childrenResults,2);
            for (int place : childrenBest){
                System.out.println(place);
                family.add(children.get(place));
            }
            int [][] familyResults= new int[family.size()][4];
            for (int i = 0; i < (children.size() - 1); i++){
                for (int j = (i + 1); j < children.size(); j++){
                    System.out.println("|||||||||||pair: "+ pair+" |||||||||||||||||||||||||||||||");
                    System.out.println("Family:{ game: " + (i+1) + " X " + (j+1) + " }");
                    System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                    pairResults = compete(family.get(i), family.get(j), totalTime);
                    for (int k = 0; k < 4; k++){
                        familyResults[i][k] = familyResults[i][k] + pairResults[0][k];
                        familyResults[j][k] = familyResults[j][k] + pairResults[1][k];
                    }
                }
            }
            int[]familyBest = findTopIndices(familyResults,2);
            for (int place : familyBest){
                System.out.println(place);
                population.add(family.get(place));
            }
        }
        pool = new ArrayList<>();
        System.out.println("Mating and Selection done..");
        System.out.println("-------------secondScarcitySeason -------------");
        printPopulation();
        System.out.println("-------------secondScarcitySeason -------------");
    }

    public void firstAbundanceSeason(double totalTime, int elitismNum, int poolNum){
        rankPopulation(totalTime, elitismNum, poolNum);
        System.out.println("ranking done..");
        offspringSelection(totalTime);
        pool.addAll(population);
        System.out.println("-------------firstAbundanceSeason -------------");
        printPopulation();
        System.out.println("-------------firstAbundanceSeason -------------");
    }

    public void offspringSelection(double totalTime){
        System.out.println("Offspring Selection started..");
        families = new ArrayList<>();
        System.out.println("Mating started..");
        Individual[] offspring;
        Collections.shuffle(pool, random);
        for (int i = 0; i < pool.size() - 1; i += 2) {
            System.out.println("-------------------------------");
            System.out.println("pair: " + i + " and " + (i + 1));
            Individual firstParent = pool.get(i);
            Individual secondParent = pool.get(i + 1);
            System.out.println("parents were chosen..");
            family = new ArrayList<>();
            System.out.println("+++++++++++ Parents +++++++++++");
            System.out.println(firstParent);
            System.out.println(secondParent);
            System.out.println("+++++++++++ Parents +++++++++++");
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
        int [][] familyResults;
        int [][] pairResults;
        for (int i = 0; i < families.size(); i++){
            family = families.get(i);
            familyResults = new int[4][4];
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
            for (int m = 0 ; m < 4; m++){
                String s = "";
                for (int n = 0 ; n < 4; n++){
                    s += familyResults[m][n] + " | ";
                }
                System.out.println(s);
            }
            System.out.println("////////////////////////");
        }
    }

    public void secondAbundanceSeason(double totalTime, int elitismNum, int poolNum){
        rankPopulation(totalTime, elitismNum, poolNum);
        System.out.println("ranking done..");
        fullGenerationSelection(totalTime);
        System.out.println("-------------secondAbundanceSeason -------------");
        printPopulation();
        System.out.println("-------------secondAbundanceSeason -------------");
    }
    public void fullGenerationSelection(double totalTime){
        System.out.println("Full Generation Selection started..");
        children = new ArrayList<>();
        System.out.println("Mating started..");
        Individual[] offspring;
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
            System.out.println("applying crossover..");
            offspring = firstParent.crossover(secondParent);
            if (offspring != null && offspring.length > 0) {
                children.addAll(Arrays.asList(offspring));
            } else {
                System.out.println("Crossover failed for pair: " + i + " and " + (i + 1));
                while (true){
                    offspring = firstParent.crossover(secondParent);
                    if (offspring != null && offspring.length > 0) {
                        children.addAll(Arrays.asList(offspring));
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
        int [][] generationResults = new int[children.size()][4];
        int [][] pairResults;
        for (int i = 0; i < (children.size() - 1); i++){
            for (int j = (i + 1); j < children.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("new generation:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(children.get(i), children.get(j), totalTime);
                for (int k = 0; k < 4; k++){
                    generationResults[i][k] = generationResults[i][k] + pairResults[0][k];
                    generationResults[j][k] = generationResults[j][k] + pairResults[1][k];
                }
            }
        }
        int[]generationBest = findTopIndices(generationResults,children.size() / 2);
        for (int index : generationBest){
            System.out.println(index);
            population.add(children.get(index));
        }
        System.out.println("---------------------");
        System.out.println("---------------------");
        System.out.println("---------------------");
        for (int m = 0 ; m < children.size(); m++){
            String s = "";
            for (int n = 0 ; n < 4; n++){
                s += generationResults[m][n] + " | ";
            }
            System.out.println(s);
        }
        System.out.println("////////////////////////");
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

    public void fullYear(double totalTime, int elitismNum, int poolNum){
        if (this.generation % 4 == 0){
            System.out.println("-------------firstScarcitySeason -------------");
            printGenerationNumber();
            firstScarcitySeason(totalTime);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
        }

        if (this.generation % 4 == 1){
            System.out.println("-------------firstAbundanceSeason -------------");
            printGenerationNumber();
            firstAbundanceSeason(totalTime, elitismNum, poolNum);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
        }

        if (this.generation % 4 == 2){
            System.out.println("-------------secondScarcitySeason -------------");
            printGenerationNumber();
            secondScarcitySeason(totalTime);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
        }

        if (this.generation % 4 == 3){
            System.out.println("-------------secondAbundanceSeason -------------");
            printGenerationNumber();
            secondAbundanceSeason(totalTime, elitismNum, poolNum);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
        }
        printPopulation();
    }

    public void firstSixtyFourOptimization(){
        if (this.generation < 1) initiatePopulation();
        System.out.println("0000000000000000000000000000000000000000000000");
        printPopulation();
        for (int i = this.generation; i < 40; i += 4){
            if (i == 8 || i == 12 || i == 24 || i == 28){
                explore();
            }
            else if (i == 16 || i == 32) {
                normalMode();
            }
            fullYear(5000,6,4);
        }

        for (int j = this.generation; j < 48; j += 4){
            explore();
            fullYear(6000,6,4);
        }
        for (int m = this.generation; m < 52; m += 4){
            if (this.generation == 48) {
                normalMode();
            }
            fullYear(7000,6,4);
        }

        for (int k = this.generation; k < 64; k += 4){
            if (k == 56){
                exploit();
            }
            else if (k == 60) {
                normalMode();
            }
            fullYear(8000,6,4);
        }
        System.out.println();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||| Survival of the fittest 8 |||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println();
        rankPopulation(10000,8,0);
        savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
    }
    public void secondThirtyTwo(){
        for (int i = this.generation; i < 96; i += 4){
            if (i == 68 || i == 72){
                explore();
            }
            else if (i == 88){
                exploit();
            }
            else if (i == 76 || i == 92){
                normalMode();
            }
            fullYear(12000,4,4);
        }
        System.out.println();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||| Survival of the fittest 6 |||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println();
        rankPopulation(16000,6,0);
        savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
    }

    public void thirdSixteen(){
        for (int i = this.generation; i < 112; i += 4){
            printPopulation();
            if (i == 100 || i == 104){
                explore();
            }
            else if (i == 108){
                normalMode();
            }
            fullYear(24000,4,2);
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");

        }
        System.out.println();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||| Survival of the fittest 4 |||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println();
        rankPopulation(28000,4,0);
        savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");

    }

    public void fourthEight(){
        for (int i = this.generation; i < 120; i += 4){
            printPopulation();
            if (i == 112){
                explore();
            }
            else if (i == 116){
                normalMode();
            }
            fullYear(30000,2,2);
        }
        System.out.println();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||| Survival of the fittest 2 |||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println();
        rankPopulation(36000,2,0);
        savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
    }

    public void famine(){
        System.out.println();
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("||||||||||||||||||| !!!!!!!! FAMINE !!!!!!!!! |||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");
        System.out.println();

        for (int i = this.generation; i < 124; i++){
            printGenerationNumber();
            if (i < 122){
                explore();
            }
            else if (i == 122){
                normalMode();
            }
            else{
                exploit();
            }
            firstScarcitySeason(48000);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
            printPopulation();
        }


        for (int i = this.generation; i < 126; i++){
            printGenerationNumber();
            if (i == 125){
                exploit();
            }
            firstScarcitySeason(60000);
            generation++ ;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
            printPopulation();
        }

        for (int i = this.generation; i < 128; i++){
            printGenerationNumber();
            if (i == 127){
                exploit();
            }
            firstScarcitySeason(90000);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
            printPopulation();
        }

        for (int i = this.generation; i < 130; i++){
            printGenerationNumber();
            if (i == 129){
                exploit();
            }
            firstScarcitySeason(120000);
            generation++;
            savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
            printPopulation();
        }

        printGenerationNumber();
        rankPopulation(120000,1,0);
        generation++;
        savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");
        printPopulation();

        printGenerationNumber();
        normalMode();
        best = population.get(0);
        savePlayerCheckpoint("checkpoint_7_" + generation + ".ser");

        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("0000000000000000000  BEST  00000000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println(best);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("0000000000000000000  BEST  00000000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");

    }

    public void fullOptimization(){
        if (this.generation < 64) firstSixtyFourOptimization();
        if (this.generation < 96) secondThirtyTwo();
        if (this.generation < 112) thirdSixteen();
        if (this.generation < 120) fourthEight();
        famine();
    }
    public static void main (String[] args){
        //PlayerAndMovesHabitat playerAndMovesHabitat = new PlayerAndMovesHabitat();
        PlayerAndMovesHabitat playerAndMovesHabitat = loadPlayerCheckpoint("checkpoint_7_41.ser");
        playerAndMovesHabitat.fullOptimization();
    }
}
