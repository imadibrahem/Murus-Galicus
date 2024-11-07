package model;

import model.object.ObjectBoard;
import model.player.Player;
import model.player.User;
import view.DisplayBoard;
import view.DisplayFrame;
import view.UserInput;

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

        while (true){
            playerOn.makeMove(playerOn.decideMove());
            FEN = playerOn.getBoard().generateFEN();
            displayBoard.updateBoard(FEN);
            playerOn = playerOn.isEvaluationBlue()? red : blue;
            playerOn.getBoard().build(FEN);
            blue.switchTurn();
            red.switchTurn();
            userInput.setPlayer(playerOn);

        }
    }

}
