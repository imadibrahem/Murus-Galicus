package model.evolutionTheory.evaluationFunction;

import model.Board;
import model.Game;
import model.bit.BitBoard;
import model.evaluationFunction.EvaluationFunction;
import model.evolutionTheory.individual.Individual;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveGeneratorEvolutionTheory;
import model.move.MoveType;
import model.player.Player;
import model.player.TranspositionTablePlayer;
import view.UserInput;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EvaluationFunctionHabitat implements Serializable {
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
    MoveType[] moveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
    int [] directions = {1, 8, 2, 3, 7, 6, 4, 5};
    int generation = 0;
    public Individual best;


    public void saveCheckpoint(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            //out.writeObject(this);
            out.writeObject(pool);
            out.writeObject(families);
            out.writeObject(family);
            out.writeObject(children);
            out.writeObject(population);
            out.writeObject(populationNew);
            out.writeObject(best);
            out.writeInt(generation);    // Save generation count
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EvaluationFunctionHabitat loadCheckpoint(String filename) {
        EvaluationFunctionHabitat evaluationFunctionHabitat = new EvaluationFunctionHabitat();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            evaluationFunctionHabitat.pool = (List<Individual>) in.readObject();
            evaluationFunctionHabitat.families = (List<List<Individual>>) in.readObject();
            evaluationFunctionHabitat.family = (List<Individual>) in.readObject();
            evaluationFunctionHabitat.children = (List<Individual>) in.readObject();
            evaluationFunctionHabitat.population = (List<Individual>) in.readObject();
            evaluationFunctionHabitat.populationNew = (List<Individual>) in.readObject();
            evaluationFunctionHabitat.best = (Individual)in.readObject();
            evaluationFunctionHabitat.generation = in.readInt();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return evaluationFunctionHabitat;
    }

    public void initiatePopulation(){
        for (int i = 0; i < 10; i++) population.add(new EvaluationFunctionIndividual());
    }

    public void firstScarcitySeason(int depth){
        pool.addAll(population);
        families = new ArrayList<>();
        population = new ArrayList<>();
        while (!pool.isEmpty()){
            family = new ArrayList<>();
            int index = random.nextInt(pool.size());
            Individual firstParent = pool.remove(index);
            index = random.nextInt(pool.size());
            Individual secondParent = pool.remove(index);
            family.add(firstParent);
            family.add(secondParent);
            Individual[] offspring = firstParent.crossover(secondParent);
            family.addAll(Arrays.asList(offspring));
            families.add(family);
        }
        fullFamilySelection(depth);
        System.out.println("-------------firstScarcitySeason -------------");
        printPopulation();
        System.out.println("-------------firstScarcitySeason -------------");
    }

    public void firstAbundanceSeason(int depth, int elitismNum, int poolNum){
        rankPopulation(depth, elitismNum, poolNum);
        offspringSelection(depth);
        pool.addAll(population);
        System.out.println("-------------firstAbundanceSeason -------------");
        printPopulation();
        System.out.println("-------------firstAbundanceSeason -------------");
    }

    public void secondAbundanceSeason(int depth, int elitismNum, int poolNum){
        rankPopulation(depth, elitismNum, poolNum);
        fullGenerationSelection(depth);
        System.out.println("-------------secondAbundanceSeason -------------");
        printPopulation();
        System.out.println("-------------secondAbundanceSeason -------------");
    }

    public void secondScarcitySeason(int depth){
        families = new ArrayList<>();
        population = new ArrayList<>();
        int pair = 0;
        while (!pool.isEmpty()){
            pair++;
            family = new ArrayList<>();
            children = new ArrayList<>();
            int index = random.nextInt(pool.size());
            Individual firstParent = pool.remove(index);
            index = random.nextInt(pool.size());
            Individual secondParent = pool.remove(index);
            family.add(firstParent);
            family.add(secondParent);
            Individual[] offspring = firstParent.crossover(secondParent);
            children.addAll(Arrays.asList(offspring));
            int [][] pairResults;
            int [][] childrenResults= new int[children.size()][4];
            for (int i = 0; i < (children.size() - 1); i++){
                for (int j = (i + 1); j < children.size(); j++){
                    System.out.println("|||||||||||pair: "+ pair+" |||||||||||||||||||||||||||||||");
                    System.out.println("Children:{ game: " + (i+1) + " X " + (j+1) + " }");
                    System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                    pairResults = compete(children.get(i), children.get(j), depth);
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
                    pairResults = compete(family.get(i), family.get(j), depth);
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
        System.out.println("-------------secondScarcitySeason -------------");
        printPopulation();
        System.out.println("-------------secondScarcitySeason -------------");
    }

    public void offspringSelection(int depth){
        families = new ArrayList<>();
        while (!pool.isEmpty()){
            family = new ArrayList<>();
            int index = random.nextInt(pool.size());
            Individual firstParent = pool.remove(index);
            index = random.nextInt(pool.size());
            Individual secondParent = pool.remove(index);
            Individual[] offspring = firstParent.crossover(secondParent);
            family.addAll(Arrays.asList(offspring));
            families.add(family);
        }
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
            for (int m = 0 ; m < 4; m++){
                String s = "";
                for (int n = 0 ; n < 4; n++){
                    s += familyResults[m][n] + " | ";
                }
                System.out.println(s);
            }
            printPopulation();
            System.out.println("////////////////////////");
        }
    }
    public void fullGenerationSelection(int depth){
        children = new ArrayList<>();
        while (!pool.isEmpty()){
            int index = random.nextInt(pool.size());
            Individual firstParent = pool.remove(index);
            index = random.nextInt(pool.size());
            Individual secondParent = pool.remove(index);
            Individual[] offspring = firstParent.crossover(secondParent);
            children.addAll(Arrays.asList(offspring));
        }
        int [][] generationResults = new int[children.size()][4];
        int [][] pairResults;
        for (int i = 0; i < (children.size() - 1); i++){
            for (int j = (i + 1); j < children.size(); j++){
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                System.out.println("new generation:{ game: " + (i+1) + " X " + (j+1) + " }");
                System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||");
                pairResults = compete(children.get(i), children.get(j), depth);
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
        printPopulation();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        populationNew = new ArrayList<>();
        int[]populationBest = findTopIndices(populationResults,elitismNum);
        int[]poolIndices = findTopIndices(populationResults,poolNum);
        for (int index : populationBest){
            populationNew.add(population.get(index));
        }
        for (int index : poolIndices){
            pool.add(population.get(index));
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        population = populationNew;
        populationNew = new ArrayList<>();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("////////////////////////");
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
        for (Individual individual : population) System.out.println(individual);
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

    public void fullYear(int depth, int elitismNum, int poolNum){
        if (this.generation % 4 == 0){
            System.out.println("-------------firstScarcitySeason -------------");
            printGenerationNumber();
            firstScarcitySeason(depth);
            generation++;
            saveCheckpoint("checkpoint_" + generation + ".ser");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.generation % 4 == 1){
            System.out.println("-------------firstAbundanceSeason -------------");
            printGenerationNumber();
            firstAbundanceSeason(depth, elitismNum, poolNum);
            generation++;
            saveCheckpoint("checkpoint_" + generation + ".ser");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        if (this.generation % 4 == 2){
            System.out.println("-------------secondScarcitySeason -------------");
            printGenerationNumber();
            secondScarcitySeason(depth);
            generation++;
            saveCheckpoint("checkpoint_" + generation + ".ser");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (this.generation % 4 == 3){
            System.out.println("-------------secondAbundanceSeason -------------");
            printGenerationNumber();
            secondAbundanceSeason(depth, elitismNum, poolNum);
            generation++;
            saveCheckpoint("checkpoint_" + generation + ".ser");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printPopulation();
    }

    public void explore(){
        for (Individual individual : population)individual.explore();
    }

    public void exploit(){
        for (Individual individual : population)individual.exploit();

    }

    public void normalMode(){
        for (Individual individual : population)individual.normalMode();
    }

    public void firstHundredOptimization(){
        if (this.generation < 1) initiatePopulation();
        System.out.println("000000000000000000000");
        printPopulation();
        for (int i = this.generation; i < 40; i += 4){
            if (i == 20) explore();
            else if (i == 24) normalMode();
            fullYear(2,6,4);
        }

        for (int j = this.generation; j < 48; j += 4){
            explore();
            fullYear(2,6,4);
         }
        for (int m = this.generation; m < 52; m += 4){
            if (this.generation == 48) normalMode();
            fullYear(2,6,4);

        }

        for (int k = this.generation; k < 60; k += 4){
            exploit();
            fullYear(2,6,4);
        }

        for (int l = this.generation; l < 100; l += 4){
            if (l == 72 || l == 84) explore();
            else if (l == 60 ||l == 76 || l == 88) normalMode();
            fullYear(2,6,4);
        }
        rankPopulation(3,8,0);
        saveCheckpoint("checkpoint_" + generation + ".ser");
    }

    public void secondFortyEight(){
        for (int i = this.generation; i < 148; i += 4){
            if (i == 116 || i == 132) explore();
            else if (i == 124)exploit();
            else if (i == 120 || i == 128 || i == 136) normalMode();
            fullYear(3,4,4);
        }
        rankPopulation(4,6,0);
        saveCheckpoint("checkpoint_" + generation + ".ser");

    }

    public void thirdTwentyFour(){
        for (int i = this.generation; i < 172; i += 4){
            if (i == 156 || i == 164) explore();
            else if (i == 160 || i == 168) normalMode();
            fullYear(4,4,2);
        }
        rankPopulation(5,4,0);
    }

    public void fourthTwelve(){
        for (int i = this.generation; i < 184; i += 4){
            if (i == 176) explore();
            else if (i == 180) normalMode();
            fullYear(5,2,2);
        }
        rankPopulation(6,2,0);
    }

    public void famine(){
        for (int i = this.generation; i < 188; i++){
            generation++;
            printGenerationNumber();
            if (i < 186) explore();
            else if (i == 186)normalMode();
            else exploit();
            firstScarcitySeason(6);
            saveCheckpoint("checkpoint_" + generation + ".ser");
            printPopulation();
        }

        if (this.generation < 189){
            generation++;
            printGenerationNumber();
            exploit();
            firstScarcitySeason(7);
            saveCheckpoint("checkpoint_" + generation + ".ser");
            printPopulation();
        }

        if (this.generation < 190){
            generation++;
            printGenerationNumber();
            exploit();
            firstScarcitySeason(8);
            saveCheckpoint("checkpoint_" + generation + ".ser");
            printPopulation();

        }

        if (this.generation < 191){
            generation++;
            printGenerationNumber();
            exploit();
            firstScarcitySeason(9);
            saveCheckpoint("checkpoint_" + generation + ".ser");
            printPopulation();
        }

        generation++;
        printGenerationNumber();
        rankPopulation(10,1,0);
        saveCheckpoint("checkpoint_" + generation + ".ser");
        printPopulation();

        normalMode();
        best = population.get(0);
        saveCheckpoint("checkpoint_done.ser");

        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("0000000000000000000  BEST  00000000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println(best);
        System.out.println("00000000000000000000000000000000000000000000000");
        System.out.println("0000000000000000000  BEST  00000000000000000000");
        System.out.println("00000000000000000000000000000000000000000000000");

    }

    public void fullOptimization(){
        if (this.generation < 100) firstHundredOptimization();
        if (this.generation < 148) secondFortyEight();
        if (this.generation < 172) thirdTwentyFour();
        if (this.generation < 184) fourthTwelve();
        famine();
    }

    public static void main (String[] args){
        EvaluationFunctionHabitat evaluationFunctionHabitat = loadCheckpoint("checkpoint_190.ser");
        //EvaluationFunctionHabitat evaluationFunctionHabitat = new EvaluationFunctionHabitat();
        evaluationFunctionHabitat.fullOptimization();
    }

}
