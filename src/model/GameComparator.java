package model;

import model.move.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void compareBoardsFunctions(){
        //int[] distancesValues = {1, 2, 3, 4, 5, 6, 7};
        int[] distancesValues = {1, 1, 1, 1, 1, 1, 1};

        //int[] columnsValues = {1, 2, 3, 4};
        int[] columnsValues = {1, 1, 1, 1};

        while (firstGame.winner == null){
            compareMoveGenerating();
            firstGame.playRound();
            secondGame.makeRound(firstGame.moves.peekLast());
            compareGameStates();
            firstGame.switchPlayer();
            secondGame.switchPlayer();
            firstGame.checkForWinner();
            compareDistancesAndColumns(distancesValues, columnsValues);
            compareIsolatedPieces();
            color = firstGame.playerOn.isEvaluationBlue() ? "Blue Player" : "Red Player";
        }
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
                        + " got the following moves mre than the Second game: " + differencesFirst);

                List<Move> differencesSecond = new ArrayList<>(secondGamePlayerOnAllStyles.get(i));
                differencesSecond.removeAll(firstGamePlayerOnAllStyles.get(i));
                if (!differencesSecond.isEmpty()) System.out.println("\n" + "Second game " + color
                        + " got the following moves mre than the First game: " + differencesSecond);

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

    public void compareDistancesAndColumns(int[] distancesValues, int[] columnsValues){
        int firstBlueWallsDistances = firstGame.blueBoard.wallsDistances(true, distancesValues);
        int firstBlueTowersDistances = firstGame.blueBoard.towersDistances(true, distancesValues);
        int firstBlueWallsColumns = firstGame.blueBoard.wallsColumns(true, columnsValues);
        int firstBlueTowersColumns = firstGame.blueBoard.towersColumns(true, columnsValues);

        int firstRedWallsDistances = firstGame.redBoard.wallsDistances(false, distancesValues);
        int firstRedTowersDistances = firstGame.redBoard.towersDistances(false, distancesValues);
        int firstRedWallsColumns = firstGame.redBoard.wallsColumns(false, columnsValues);
        int firstRedTowersColumns = firstGame.redBoard.towersColumns(false, columnsValues);

        int secondBlueWallsDistances = secondGame.blueBoard.wallsDistances(true, distancesValues);
        int secondBlueTowersDistances = secondGame.blueBoard.towersDistances(true, distancesValues);
        int secondBlueWallsColumns = secondGame.blueBoard.wallsColumns(true, columnsValues);
        int secondBlueTowersColumns = secondGame.blueBoard.towersColumns(true, columnsValues);

        int secondRedWallsDistances = secondGame.redBoard.wallsDistances(false, distancesValues);
        int secondRedTowersDistances = secondGame.redBoard.towersDistances(false, distancesValues);
        int secondRedWallsColumns = secondGame.redBoard.wallsColumns(false, columnsValues);
        int secondRedTowersColumns = secondGame.redBoard.towersColumns(false, columnsValues);
/*
            System.out.println("!!!!!!!!!!!!!!Blue Walls Distances !!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueWallsDistances + " second = " + secondBlueWallsDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!! !!! Blue Towers Distances !!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueTowersDistances + " second = " + secondBlueTowersDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!!!!  PROBLEM in Blue Walls Columns  !!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueWallsColumns + " second = " + secondBlueWallsColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!!!!!!!!!!  Blue Towers Columns !!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstBlueTowersColumns + " second = " + secondBlueTowersColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!!!!!!!!! Red Walls Distances !!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedWallsDistances + " second = " + secondRedWallsDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!!!!!!! Red Towers Distances !!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedTowersDistances + " second = " + secondRedTowersDistances + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!!!!!!!!! Red Walls Columns !!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedWallsColumns + " second = " + secondRedWallsColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

            System.out.println("!!!!!!!!!!!!!!! Red Towers Columns !!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstRedTowersColumns + " second = " + secondRedTowersColumns + " !!!!!!!!!!!!!!!!!");
            System.out.println("******************************************************");

        System.out.println();
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println();

 */

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

}
