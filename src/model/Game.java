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
        switchPlayer();
        checkForWinner();
    }

    public void makeRound(Move move) {
        rounds++;
        moves.add(move);
        history.add(move.getValue());
        playerOn.makeMove(move);
        FEN = playerOn.getBoard().generateFEN();
        colorOldMove(playerOn.isEvaluationBlue(), move);
        displayBoard.updateBoard(FEN);
        switchPlayer();

    }

    public void playGame() {
        while (winner == null)playRound();
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
        String FenInitial = "1ttttttt/ww6/T7/8/1W6/1WW5/1T1WTTTT,b";
        //FenInitial = "1ttttttt/ww6/T7/8/1W6/1WW5/1T1W4,b";
        MoveType[] moveTypes = {MoveType.FRIEND_ON_BOTH, MoveType.FRIEND_ON_NEAR, MoveType.FRIEND_ON_FAR, MoveType.QUIET, MoveType.SACRIFICE};
        int [] directions = {1, 8, 2, 3, 7, 6, 4, 5};

        Board blueBoard = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator = new MoveGeneratorEvolutionTheory(blueBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player blue = new RandomPlayer(true, blueBoard,blueGenerator);
        //Player blue = new User(true, blueBoard,userInput);

        Board redBoard = new ObjectBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator = new MoveGeneratorEvolutionTheory(redBoard, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player red = new RandomPlayer(false, redBoard,redGenerator);
        //Player red = new User(false, redBoard,userInput);



/*
        Board blueBoard2 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator blueGenerator2 = new MoveGeneratorEvolutionTheory(blueBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player blue2 = new RandomPlayer(true, blueBoard2,blueGenerator2);

        Board redBoard2 = new BitBoard(FenTrimmer(FenInitial));
        MoveGenerator redGenerator2 = new MoveGeneratorEvolutionTheory(redBoard2, MoveGeneratingStyle.ALL_TYPE_MOVES_PIECE_BY_PIECE,moveTypes, directions, true );
        Player red2 = new RandomPlayer(false, blueBoard2,redGenerator2);

        Board blueBoard = new BitBoard(FenTrimmer(FenInitial));
        Player blue = new User(true,blueBoard,userInput);


        Board redBoard = new ObjectBoard(FenTrimmer(FenInitial));
        Player red = new User(false,redBoard,userInput);

 */

        Game game = new Game(userInput, red, blue, FenInitial);
        //Game game2 = new Game(userInput, red2, blue2, FenInitial);
        game.playGame();
/*
        List<List<Move>> allStyles = blue.getMoveGenerator().generateAllStyles(true);
        System.out.println("555555555555555555555555555555555555555");
        System.out.println("555555555555555555555555555555555555555");

        List<List<Move>> allStyles2 = blue2.getMoveGenerator().generateAllStyles(true);

        for (int i = 0; i < allStyles.size(); i++){
            System.out.println("***************************************************");
            System.out.println("***************************************************");
            System.out.println("***************************************************");
            if (!allStyles.get(i).equals(allStyles2.get(i))){
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!!!!!  PROBLEM in style nr." +((i+2)/2)+"!!!!!!!!!!!");
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                Set<Move> clean = new HashSet<>();
                Set<Move> duplicates = new HashSet<>();
                for (Move move : allStyles.get(i)){
                    if (!clean.add(move)) duplicates.add(move);
                }
                if (!duplicates.isEmpty()) System.out.println("object got duplicates" + duplicates);

                Set<Move> clean2 = new HashSet<>();
                Set<Move> duplicates2 = new HashSet<>();
                for (Move move : allStyles2.get(i)){
                    if (!clean2.add(move)) duplicates2.add(move);
                }
                if (!duplicates2.isEmpty()) System.out.println("bit got duplicates" + duplicates2);

                List<Move> differences = new ArrayList<>(allStyles.get(i));
                differences.removeAll(allStyles2.get(i));
                if (!differences.isEmpty()) System.out.println("object got more moves: " + differences);

                List<Move> differences2 = new ArrayList<>(allStyles2.get(i));
                differences2.removeAll(allStyles.get(i));
                if (!differences2.isEmpty()) System.out.println("bit got more moves: " + differences2);

                System.out.println("sorting:");
            }

            System.out.println(allStyles.get(i));
            System.out.println("***************************************************");
            System.out.println(allStyles2.get(i));
        }
        System.out.println("***************************************************");
        System.out.println("***************************************************");
        System.out.println("***************************************************");

 */


    }

}
