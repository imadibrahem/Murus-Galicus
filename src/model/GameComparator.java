package model;

import model.move.Move;
import model.player.Player;

import java.util.*;

public class GameComparator {
    Game firstGame;
    Game secondGame;
    List<List<Move>> firstGamePlayerOnAllStyles;
    List<List<Move>> secondGamePlayerOnAllStyles;
    String color;

    public GameComparator(Game firstGame, Game secondGame) {
        color = firstGame.playerOn.isEvaluationBlue() ? "Blue Player" : "Red Player";
        this.firstGame = firstGame;
        this.secondGame = secondGame;
    }

    public void compareBoardsFunctions(boolean makeUnmake){
        while (firstGame.winner == null){
            compareMoveGenerating();
            if (makeUnmake) makeAndUnmakeAllMovesTester();
            firstGame.playRound();
            secondGame.makeRound(firstGame.moves.peekLast());
            compareGameStates();
            firstGame.switchPlayer();
            secondGame.switchPlayer();
            firstGame.checkForWinner();
            compareDistancesAndColumns();
            compareIsolatedPieces();
            compareEvaluation();
            //compareHashing();
            color = firstGame.playerOn.isEvaluationBlue() ? "Blue Player" : "Red Player";
        }
        //secondGame.rewindGame();
        //firstGame.rewindGame();
    }

    public void compareMoveGenerating(){
        firstGamePlayerOnAllStyles = firstGame.playerOn.getMoveGenerator().generateAllStyles(firstGame.playerOn.isEvaluationBlue());
        secondGamePlayerOnAllStyles = secondGame.playerOn.getMoveGenerator().generateAllStyles(secondGame.playerOn.isEvaluationBlue());
        for (int i = 0; i < firstGamePlayerOnAllStyles.size(); i++){
            if (!firstGamePlayerOnAllStyles.get(i).equals(secondGamePlayerOnAllStyles.get(i))){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!  PROBLEM in style nr." +((i+2)/2)+"  !!!!!!!!!!!!!");
                String frontToBackOrBackToFront = i % 2 == 0 ? " Front to Back " : " Back to Front ";
                System.out.println("!!!!!!!!!!!!!! " + frontToBackOrBackToFront + " !!!!!!!!!!!!!!");

                Set<Move> cleanFirst = new HashSet<>();
                Set<Move> duplicatesFirst = new HashSet<>();
                for (Move move : firstGamePlayerOnAllStyles.get(i)){
                    if (!cleanFirst.add(move)) duplicatesFirst.add(move);
                }
                if (!duplicatesFirst.isEmpty()) System.out.println("\n" + "First game " + color + " got duplicates: " + duplicatesFirst);

                Set<Move> cleanSecond = new HashSet<>();
                Set<Move> duplicatesSecond = new HashSet<>();
                for (Move move : secondGamePlayerOnAllStyles.get(i)){
                    if (!cleanSecond.add(move)) duplicatesSecond.add(move);
                }
                if (!duplicatesSecond.isEmpty()) System.out.println("\n" + "Second game " + color + " got duplicates: " + duplicatesSecond);

                List<Move> differencesFirst = new ArrayList<>(firstGamePlayerOnAllStyles.get(i));
                differencesFirst.removeAll(secondGamePlayerOnAllStyles.get(i));
                if (!differencesFirst.isEmpty()) System.out.println("\n" + "First game " + color
                        + " got the following moves more than the Second game: " + differencesFirst);

                List<Move> differencesSecond = new ArrayList<>(secondGamePlayerOnAllStyles.get(i));
                differencesSecond.removeAll(firstGamePlayerOnAllStyles.get(i));
                if (!differencesSecond.isEmpty()) System.out.println("\n" + "Second game " + color
                        + " got the following moves more than the First game: " + differencesSecond);

                System.out.println("**** Sorting: ****");
                System.out.println(firstGamePlayerOnAllStyles.get(i));
                System.out.println("\n" + secondGamePlayerOnAllStyles.get(i));
                System.out.println("***************************************************");
            }
        }
    }

    public void compareGameStates(){
        int firstGameState = firstGame.playerOn.getBoard().gameState(firstGame.playerOn.isEvaluationBlue());
        int secondGameState = secondGame.playerOn.getBoard().gameState(secondGame.playerOn.isEvaluationBlue());
        /*
        System.out.println("***************************************************");
        System.out.println("******************"+color+"*********************");
        System.out.println("*********************"+firstGameState+ "|" +secondGameState +"***************************");
         */
        if (firstGameState != secondGameState){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! PROBLEM in GAME STATE !!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!"+color+"!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstGameState + " second = " + secondGameState + " !!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println();
        }
    }

    public void compareDistancesAndColumns(){
        int[] towersDistancesValues = firstGame.blue.getEvaluationFunction().getTowersDistancesFactor();
        int[] towersColumnsValues = firstGame.blue.getEvaluationFunction().getTowersColumnsFactor();
        int[] wallsDistancesValues = firstGame.blue.getEvaluationFunction().getWallsDistancesFactor();
        int[] wallsColumnsValues = firstGame.blue.getEvaluationFunction().getWallsColumnsFactor();

        int firstBlueWallsDistances = firstGame.blueBoard.wallsDistances(true, wallsDistancesValues);
        int firstBlueTowersDistances = firstGame.blueBoard.towersDistances(true, towersDistancesValues);
        int firstBlueWallsColumns = firstGame.blueBoard.wallsColumns(true, wallsColumnsValues);
        int firstBlueTowersColumns = firstGame.blueBoard.towersColumns(true, towersColumnsValues);

        int firstRedWallsDistances = firstGame.redBoard.wallsDistances(false, wallsDistancesValues);
        int firstRedTowersDistances = firstGame.redBoard.towersDistances(false, towersDistancesValues);
        int firstRedWallsColumns = firstGame.redBoard.wallsColumns(false, wallsColumnsValues);
        int firstRedTowersColumns = firstGame.redBoard.towersColumns(false, towersColumnsValues);

        int secondBlueWallsDistances = secondGame.blueBoard.wallsDistances(true, wallsDistancesValues);
        int secondBlueTowersDistances = secondGame.blueBoard.towersDistances(true, towersDistancesValues);
        int secondBlueWallsColumns = secondGame.blueBoard.wallsColumns(true, wallsColumnsValues);
        int secondBlueTowersColumns = secondGame.blueBoard.towersColumns(true, towersColumnsValues);

        int secondRedWallsDistances = secondGame.redBoard.wallsDistances(false, wallsDistancesValues);
        int secondRedTowersDistances = secondGame.redBoard.towersDistances(false, towersDistancesValues);
        int secondRedWallsColumns = secondGame.redBoard.wallsColumns(false, wallsColumnsValues);
        int secondRedTowersColumns = secondGame.redBoard.towersColumns(false, towersColumnsValues);


        if (firstBlueWallsDistances != secondBlueWallsDistances){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Blue Walls Distances !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueWallsDistances + " second = " + secondBlueWallsDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstBlueTowersDistances != secondBlueTowersDistances){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!  PROBLEM in Blue Towers Distances !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueTowersDistances + " second = " + secondBlueTowersDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstBlueWallsColumns != secondBlueWallsColumns){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Blue Walls Columns  !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueWallsColumns + " second = " + secondBlueWallsColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstBlueTowersColumns != secondBlueTowersColumns){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Blue Towers Columns !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueTowersColumns + " second = " + secondBlueTowersColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstRedWallsDistances != secondRedWallsDistances){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Red Walls Distances !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedWallsDistances + " second = " + secondRedWallsDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstRedTowersDistances != secondRedTowersDistances){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!  PROBLEM in Red Towers Distances !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedTowersDistances + " second = " + secondRedTowersDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstRedWallsColumns != secondRedWallsColumns){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Red Walls Columns !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedWallsColumns + " second = " + secondRedWallsColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstRedTowersColumns != secondRedTowersColumns){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Red Towers Columns !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedTowersColumns + " second = " + secondRedTowersColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }

    }
    public void compareIsolatedPieces(){
        int firstBlueIsolatedWalls = firstGame.blueBoard.isolatedWallsNumber(true);
        int firstBlueIsolatedTowers = firstGame.blueBoard.isolatedTowersNumber(true);

        int firstRedIsolatedWalls = firstGame.redBoard.isolatedWallsNumber(false);
        int firstRedIsolatedTowers = firstGame.redBoard.isolatedTowersNumber(false);

        int secondBlueIsolatedWalls = secondGame.blueBoard.isolatedWallsNumber(true);
        int secondBlueIsolatedTowers = secondGame.blueBoard.isolatedTowersNumber(true);

        int secondRedIsolatedWalls = secondGame.redBoard.isolatedWallsNumber(false);
        int secondRedIsolatedTowers = secondGame.redBoard.isolatedTowersNumber(false);

        if (firstBlueIsolatedWalls != secondBlueIsolatedWalls){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Isolated Blue Walls !!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueIsolatedWalls + " second = " + secondBlueIsolatedWalls + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }

        if (firstBlueIsolatedTowers != secondBlueIsolatedTowers){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Isolated Blue Towers !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueIsolatedTowers + " second = " + secondBlueIsolatedTowers + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }

        if (firstRedIsolatedWalls != secondRedIsolatedWalls){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Isolated Red Walls !!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedIsolatedWalls + " second = " + secondRedIsolatedWalls + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }

        if (firstRedIsolatedTowers != secondRedIsolatedTowers){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!  PROBLEM in Isolated Red Towers !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedIsolatedTowers + " second = " + secondRedIsolatedTowers + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }

    }

    public void compareEvaluation(){
        int firstBlue = firstGame.blue.getEvaluationFunction().evaluate(true, 0);
        int firstRed = firstGame.red.getEvaluationFunction().evaluate(false, 0);
        int secondBlue = secondGame.blue.getEvaluationFunction().evaluate(true, 0);
        int secondRed = secondGame.red.getEvaluationFunction().evaluate(false, 0);
        if (firstBlue != secondBlue){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!! PROBLEM in Evaluation function: Blue !!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first Blue = " + firstBlue + " second Blue = " + secondBlue + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
        if (firstRed != secondRed){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!! PROBLEM in Evaluation function: Red !!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first Red  = " + firstRed + " second Red  = " + secondRed + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");
        }
/*
        System.out.println("!!!!!!!!!!!!!! first Blue = " + firstBlue + " second Blue = " + secondBlue + " !!!!!!!!!!!!!!!!!");
        System.out.println("!!!!!!!!!!!!!! first Red  = " + firstRed + " second Red  = " + secondRed + " !!!!!!!!!!!!!!!!!");
        System.out.println("******************************************************");

 */

    }

    public void makeAndUnmakeAllMovesTester(){
        List<Move> firstPlayerOnMoves = firstGame.playerOn.getMoveGenerator().generateMoves(firstGame.playerOn.isEvaluationBlue());
        List<Move> secondPlayerOnMoves = secondGame.playerOn.getMoveGenerator().generateMoves(secondGame.playerOn.isEvaluationBlue());

        for (Move move : firstPlayerOnMoves){
            String before = firstGame.playerOn.getBoard().printBoard(firstGame.playerOn.isEvaluationBlue());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firstGame.makeRound(move);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firstGame.unmakeRound(move);
            String after = firstGame.playerOn.getBoard().printBoard(firstGame.playerOn.isEvaluationBlue());
            if (!before.equals(after)){
                System.out.println("******************************************************");
                System.out.println("************** First Game : "+ move +" ****************");
                System.out.println("******************************************************");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!!!!!!! BEFORE !!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(before);
                System.out.println("!!!!!!!!!!!!!!!!!!!! AFTER !!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(after);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println();
            }
        }

        for (Move move : secondPlayerOnMoves){
            String before = secondGame.playerOn.getBoard().printBoard(secondGame.playerOn.isEvaluationBlue());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            secondGame.makeRound(move);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            secondGame.unmakeRound(move);
            String after = secondGame.playerOn.getBoard().printBoard(secondGame.playerOn.isEvaluationBlue());
            if (!before.equals(after)){
                System.out.println("******************************************************");
                System.out.println("************** Second Game : "+ move +" ***************");
                System.out.println("******************************************************");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!!!!!!! BEFORE !!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(before);
                System.out.println("!!!!!!!!!!!!!!!!!!!! AFTER !!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(after);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println();
            }
        }
    }

    public void compareHashing(){
        long firstBlueHash = firstGame.blue.getZobristHashing().getHash();
        long secondBlueHash = secondGame.blue.getZobristHashing().getHash();
        long firstRedHash = firstGame.red.getZobristHashing().getHash();
        long secondRedHash = secondGame.red.getZobristHashing().getHash();
        if (firstBlueHash != firstRedHash || secondBlueHash != secondRedHash || firstBlueHash != secondBlueHash){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!firstBlueHash = "+ firstBlueHash +"!!!!firstRedHash = "+ firstRedHash +"!!!");
            System.out.println("!!!!secondBlueHash = "+ secondBlueHash +"!!!!secondRedHash = "+ secondRedHash +"!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }

}
