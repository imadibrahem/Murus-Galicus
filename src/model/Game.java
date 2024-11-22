package model;

import model.bit.BitBoard;
import model.evolutionTheory.MoveGeneratorEvolutionTheory;
import model.move.Move;
import model.move.MoveGeneratingStyle;
import model.move.MoveGenerator;
import model.move.MoveType;
import model.object.ObjectBoard;
import model.player.Player;
import model.player.RandomPlayer;
import model.player.User;
import view.DisplayBoard;
import view.DisplayFrame;
import view.UserInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game {
    String initialFEN;
    String FEN;
    DisplayFrame displayFrame;
    DisplayBoard displayBoard;
    Board blueBoard;
    Board redBoard;
    Player red;
    Player blue;
    Player playerOn;
    UserInput userInput;
    List<Short> history;
    Stack <Move> moves;
    int rounds = 0;

    public Game(UserInput userInput, Player red, Player blue, String initialFEN) {
        this.red = red;
        this.blue = blue;
        this.initialFEN =initialFEN;
        history = new ArrayList<>();
        moves = new Stack<>();
        setFenAndPlayerOn();
        displayFrame = new DisplayFrame(FEN);
        displayBoard = displayFrame.getDisplayBoard();
        this.userInput = userInput;
        userInput.setDisplayBoard(displayBoard);
        setPlayer(blue);
        setPlayer(red);
    }

    public static String FenTrimmer (String initialFEN){
        String [] parts = initialFEN.split(",");
        return parts[0];
    }

    public void setFenAndPlayerOn(){
        String [] parts = initialFEN.split(",");
        FEN = parts[0];
        playerOn = parts[1].equals("b") ? blue : red;
        playerOn.setOn(true);
        short startingPlayer = (short) (playerOn.isEvaluationBlue() ? 1 : 0);
        history.add(startingPlayer);
    }

    public void setPlayer(Player player){
        if (player.isEvaluationBlue()) {
            this.blue = player;
            this.blueBoard = blue.getBoard();
            if (player instanceof User && playerOn.isEvaluationBlue())userInput.setPlayer(blue);

        }
        else{
            this.red = player;
            this.redBoard = red.getBoard();
            if (player instanceof User && !(playerOn.isEvaluationBlue()))userInput.setPlayer(red);

        }
    }

    public void switchPlayer() {
        playerOn = playerOn.isEvaluationBlue()? red : blue;
        playerOn.getBoard().build(FEN);
        blue.switchTurn();
        red.switchTurn();
        if (playerOn instanceof User)userInput.setPlayer(playerOn);
    }

    public void playRound() {
        rounds++;
        Move move = playerOn.decideMove();
        moves.add(move);
        history.add(move.getValue());
        playerOn.makeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        displayBoard.updateBoard(FEN);
    }

    public void playGame() {
        while (rounds < 70){
            playRound();
            switchPlayer();
        }
    }

    public static void main (String[] args){
        UserInput userInput = new UserInput();
        String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT,b";
        MoveType[] moveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
        int [] directions = {1, 8, 2, 3, 7, 6, 4, 5};
       /*
        Board blueBoard = new ObjectBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator = new MoveGeneratorEvolutionTheory(blueBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player blue = new RandomPlayer(true, blueBoard,blueGenerator);

        */
        Board blueBoard = new BitBoard(FenTrimmer(FenInitial));
        Player blue = new User(true,blueBoard,userInput);

        Board redBoard = new ObjectBoard(FenTrimmer(FenInitial));
        Player red = new User(false,redBoard,userInput);

        Game game = new Game(userInput, red, blue, FenInitial);
        game.playGame();
        /*
        String FEN = "tttttttt/8/8/8/8/8/TTTTTTTT";
        DisplayFrame displayFrame = new DisplayFrame(FEN);
        DisplayBoard displayBoard = displayFrame.getDisplayBoard();
        UserInput userInput =new UserInput();
        userInput.setDisplayBoard(displayBoard);
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

            System.out.println("*************************");
            System.out.println("BLUE PLAYER BOARD :");
            System.out.println(blueBoard.getArmy(true));
            System.out.println(blueBoard.getArmy(false));
            System.out.println("*************************");
            System.out.println("*************************");


           // System.out.println("RED PLAYER BOARD :");
           // System.out.println(blueBoard.getArmy(true));
            //System.out.println(blueBoard.getArmy(false));
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
        */


    }

}
