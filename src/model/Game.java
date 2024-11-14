package model;

import model.move.Move;
import model.object.ObjectBoard;
import model.player.Player;
import model.player.User;
import view.DisplayBoard;
import view.DisplayFrame;
import view.UserInput;

import java.util.Stack;

public class Game {
    public static void main (String[] args){
        String FEN = "tttttttt/8/8/8/8/8/TTTTTTTT";
        DisplayFrame displayFrame = new DisplayFrame(FEN);
        DisplayBoard displayBoard = displayFrame.getDisplayBoard();
        UserInput userInput =new UserInput(displayBoard);
        ObjectBoard redBoard = new ObjectBoard(FEN);
        ObjectBoard blueBoard = new ObjectBoard(FEN);
        Player red = new User(false,redBoard,userInput);
        Player blue = new User(true ,blueBoard,userInput);
        blue.setOn(true);
        Player playerOn = blue;
        userInput.setPlayer(blue);
        Stack <Move> moves = new Stack<>();
        int rounds = 0;

        while (rounds < 10){
            /*
            System.out.println("*************************");
            System.out.println("BLUE PLAYER BOARD :");
            System.out.println(blueBoard.getArmy(true));
            System.out.println(blueBoard.getArmy(false));
            System.out.println("*************************");
            System.out.println("*************************");

             */
            System.out.println("RED PLAYER BOARD :");
            System.out.println(blueBoard.getArmy(true));
            System.out.println(blueBoard.getArmy(false));
            System.out.println("*************************");


            Move move = playerOn.decideMove();
            moves.add(move);

            //System.out.println("+++++++++++++++++++++++++");
            //System.out.println("+++++++++++++++++++++++++");
            System.out.println("*************************");
            System.out.println("Move type is: " + move.getTargetType());
            String color = playerOn.isEvaluationBlue() ? " Blue " : " Red ";
            System.out.println("Player" + color + "has moved from " + move.getInitialLocation(playerOn.isEvaluationBlue())
                                +"th Square in the direction nr. " + move.getDirection());
            System.out.println("*************************");
            //System.out.println("+++++++++++++++++++++++++");
            //System.out.println("+++++++++++++++++++++++++");

            playerOn.makeMove(move);
            FEN = playerOn.getBoard().generateFEN();
            displayBoard.updateBoard(FEN);
            playerOn = playerOn.isEvaluationBlue()? red : blue;
            playerOn.getBoard().build(FEN);
            blue.switchTurn();
            red.switchTurn();
            userInput.setPlayer(playerOn);
            rounds++;
        }
        System.out.println("*************************");
        System.out.println("******** UNMAKE *********");
        System.out.println("*************************");
        playerOn = playerOn.isEvaluationBlue()? red : blue;
        blue.switchTurn();
        red.switchTurn();
        while (rounds > 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Move move = moves.pop();
            System.out.println("******** UNMAKE *********");
            System.out.println("Move type is: " + move.getTargetType());
            String color = playerOn.isEvaluationBlue() ? " Blue " : " Red ";
            System.out.println("Player" + color + "has moved from " + move.getInitialLocation(playerOn.isEvaluationBlue())
                    +"th Square in the direction nr. " + move.getDirection());
            System.out.println("*************************");

            playerOn.unmakeMove(move);
            FEN = playerOn.getBoard().generateFEN();
            displayBoard.updateBoard(FEN);
            playerOn = playerOn.isEvaluationBlue()? red : blue;
            playerOn.getBoard().build(FEN);
            blue.switchTurn();
            red.switchTurn();
            rounds--;
        }
    }

}
