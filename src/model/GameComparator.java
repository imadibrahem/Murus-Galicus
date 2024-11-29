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

    public void compareMoveGeneratingAndGameState(){
        while (firstGame.winner == null){
            compareMoveGenerating();
            firstGame.playRound();
            secondGame.makeRound(firstGame.moves.peekLast());
            compareGameStates();
            firstGame.switchPlayer();
            secondGame.switchPlayer();
            firstGame.checkForWinner();
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
        System.out.println("***************************************************");
        System.out.println("******************"+color+"*********************");
        System.out.println("*********************"+firstGameState+ "|" +secondGameState +"***************************");
        if (firstGameState != secondGameState){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! PROBLEM in GAME STATE !!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!"+color+"!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!! first = " + firstGameState + " second = " + secondGameState + " !!!!!!!!!!!!!!!!!");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println();
        }
    }
}
