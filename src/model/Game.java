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

import java.awt.*;
import java.util.*;
import java.util.List;

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
    Deque<Move> moves;
    int rounds = 0;
    Player winner;
    short oldMoveInitial = 0;
    short oldMoveFirst = 0;
    short oldMoveSecond = 0;

    public Game(UserInput userInput, Player red, Player blue, String initialFEN) {
        this.red = red;
        this.blue = blue;
        this.initialFEN =initialFEN;
        history = new ArrayList<>();
        moves = new ArrayDeque<>();
        setFenAndPlayerOn();
        displayFrame = new DisplayFrame(FEN);
        displayBoard = displayFrame.getDisplayBoard();
        this.userInput = userInput;
        userInput.setDisplayBoard(displayBoard);
        setPlayer(blue);
        setPlayer(red);
        winner = null;
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
        //System.out.println(move);
        moves.add(move);
        history.add(move.getValue());
        playerOn.makeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        colorOldMove(playerOn.isEvaluationBlue(), move);
        displayBoard.updateBoard(FEN);
    }

    public void makeRound(Move move) {
        rounds++;
        //System.out.println(move);
        moves.add(move);
        history.add(move.getValue());
        playerOn.makeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        colorOldMove(playerOn.isEvaluationBlue(), move);
        displayBoard.updateBoard(FEN);
    }

    public void playGame() {
        while (winner == null){
            playRound();
            switchPlayer();
            checkForWinner();
        }
    }
    public void rewindGame() {
        playerOn = playerOn.isEvaluationBlue()? red : blue;
        playerOn.getBoard().build(FEN);
        blue.switchTurn();
        red.switchTurn();
        while (rounds > 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Move move = moves.pollLast();
           // System.out.println(move);
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

    public void replayGame() {
        String [] parts = initialFEN.split(",");
        FEN = parts[0];
        playerOn = parts[1].equals("b") ? blue : red;
        Player playerOff = parts[1].equals("b") ? red : blue;
        playerOff.setOn(false);
        playerOn.setOn(true);
        red.getBoard().build(FEN);
        blue.getBoard().build(FEN);
        displayBoard.updateBoard(FEN);
        while (!moves.isEmpty()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Move move = moves.pollFirst();
            // System.out.println(move);
            playerOn.makeMove(move);
            FEN = playerOn.getBoard().generateFEN();
            displayBoard.updateBoard(FEN);
            playerOn = playerOn.isEvaluationBlue()? red : blue;
            playerOn.getBoard().build(FEN);
            blue.switchTurn();
            red.switchTurn();
        }
    }

    public void checkForWinner(){
        if (blueBoard.lostGame(true)){
            winner = red;
            System.out.println("GAME OVER RED PLAYER WON!!");
        }
        else if (redBoard.lostGame(false)){
            winner = blue;
            System.out.println("GAME OVER BLUE PLAYER WON!!");
        }
    }

    public void colorOldMove(boolean isBlue, Move move){
        displayBoard.displaySquare[oldMoveInitial].returnOldColor();
        displayBoard.displaySquare[oldMoveFirst].returnOldColor();
        displayBoard.displaySquare[oldMoveSecond].returnOldColor();
        int initial = move.getInitialLocation(isBlue);
        oldMoveInitial = (short) initial;
        if (move.isTargetEnemy()){
            oldMoveFirst = playerOn.getBoard().sacrificingMovesLocation(isBlue, initial,move.getDirection());
            displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        }
        else {
            short[] targets = playerOn.getBoard().normalMovesLocation(isBlue, initial,move.getDirection());
            oldMoveFirst = targets[0];
            oldMoveSecond = targets[1];
            displayBoard.displaySquare[oldMoveSecond].changeColor(Color.orange);
        }
        displayBoard.displaySquare[oldMoveFirst].changeColor(Color.orange);
        displayBoard.displaySquare[oldMoveInitial].changeColor(Color.BLUE);

    }

    public static void main (String[] args){

        UserInput userInput = new UserInput();
        UserInput userInput2 = new UserInput();
        String FenInitial = "tttttttt/8/8/8/8/8/TTTTTTTT,b";
        //FenInitial = "t6t/6W1/8/8/5t2/1W6/TT3W2,b";
        //FenInitial = "1tt4t/wwT/T6t/8/1W6/1WW5/1T1W3T,b";
        MoveType[] moveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
        int [] directions = {1, 8, 2, 3, 7, 6, 4, 5};

        Board blueBoard = new ObjectBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator = new MoveGeneratorEvolutionTheory(blueBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player blue = new RandomPlayer(true, blueBoard,blueGenerator);
        //Player blue = new User(true, blueBoard, blueGenerator,userInput);

        Board redBoard = new ObjectBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator = new MoveGeneratorEvolutionTheory(redBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player red = new RandomPlayer(false, redBoard,redGenerator);
        //Player red = new User(false, redBoard, redGenerator,userInput);

        Board blueBoard2 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator2 = new MoveGeneratorEvolutionTheory(blueBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player blue2 = new RandomPlayer(true, blueBoard2,blueGenerator2);

        Board redBoard2 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator2 = new MoveGeneratorEvolutionTheory(redBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player red2 = new RandomPlayer(false, redBoard2,redGenerator2);
/*
        Board blueBoard = new BitBoard(FenTrimmer(FenInitial));
        Player blue = new User(true,blueBoard,userInput);

        Board redBoard = new ObjectBoard(FenTrimmer(FenInitial));
        Player red = new User(false,redBoard,userInput);

 */

        Game game = new Game(userInput, red, blue, FenInitial);
        Game game2 = new Game(userInput2, red2, blue2, FenInitial);
        //game.playGame();
        GameComparator gameComparator = new GameComparator(game, game2);
        gameComparator.compareBoardsFunctions();

    }

}
